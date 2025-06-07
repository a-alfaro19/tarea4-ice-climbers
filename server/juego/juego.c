#include "juego.h"
#include "acciones.h"
#include "mapa.h"
#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <math.h>
#include "../red/SocketServer.h"
/**
 * Inicializa el juego colocando a Popo y Nana en sus posiciones iniciales.
 * También reinicia valores como vidas, puntaje, velocidad y estado del pterodáctilo.
 */
void inicializar_juego(Juego* juego) {
    juego->nivel_actual = 0;
    juego->en_fase_bonus = 0;
    juego->velocidad = 1;

    Jugador* popo = &juego->jugadores[0];
    Jugador* nana = &juego->jugadores[1];

    snprintf(popo->nombre, sizeof(popo->nombre), "Popo");
    snprintf(nana->nombre, sizeof(nana->nombre), "Nana");

    // Popo
    popo->x = 9;
    popo->y = 1;
    popo->y_real = (float)popo->y;
    popo->vy = 0.0f;
    popo->en_el_aire = 0;
    popo->vidas = 3;
    popo->puntaje = 0;
    popo->direccion = 'R';

    // Nana
    nana->x = 12;
    nana->y = 1;
    nana->y_real = (float)nana->y;
    nana->vy = 0.0f;
    nana->en_el_aire = 0;
    nana->vidas = 3;
    nana->puntaje = 0;
    nana->direccion = 'L';

    // Pterodáctilo inactivo
    juego->ptero.activo = 0;
    juego->ptero.x = 0;
    juego->ptero.y = 0;
    juego->ptero.direccion = 1;

}
/**
 * Reinicia el juego tras una partida terminada o una vida perdida,
 * aumentando la velocidad general como forma de progresión.
 */
void reiniciar_juego(Juego* juego) {
    juego->nivel_actual = 0;
    juego->en_fase_bonus = 0;
    juego->velocidad++;
}
/**
 * Imprime el estado general del juego en consola (debug).
 */
void imprimir_estado_juego(Juego* juego) {
    printf("Nivel: %d | Velocidad: %d | Bonus: %d\n",
           juego->nivel_actual, juego->velocidad, juego->en_fase_bonus);
}
/**
 * Determina en qué nivel está un jugador con base en su posición vertical.
 * Usa bloques cercanos o la posición real como referencia.
 */
int obtener_nivel_actual_de_jugador(Jugador* j) {
    int mejor_y = -1;
    // Buscar bloque más cercano debajo
    for (int offset = 0; offset <= 2; offset++) {
        int y_check = j->y - offset;
        if (y_check >= 0 && hay_bloque_en(j->x, y_check)) {
            mejor_y = y_check;
            break;
        }
    }

    // Si no encontró bloque cerca abajo, usa y_real (más estable)
    int base_y = (mejor_y >= 0) ? mejor_y : (int)(j->y_real + 0.5f);
    return base_y / (TOTAL_FLOOR_HEIGHT + ROWS_BETWEEN_FLOORS);
}
/**
 * Genera hasta 4 frutas aleatorias en pisos altos durante la fase bonus.
 * Solo aparecen si hay un bloque de soporte debajo y espacio libre arriba.
 */
void generar_frutas_bonus(Juego* juego) {
    static TipoFruta tipos[4] = { NARANJA, BANANO, BERENJENA, LECHUGA };
    juego->frutas.cantidad = 0;

    int intentos = 0;
    while (juego->frutas.cantidad < 4 && intentos < 500) {
        // Elegir nivel entre 9 y 14 (fase bonus)
        int nivel = 9 + rand() % 6;
        int y_suelo = nivel * (TOTAL_FLOOR_HEIGHT + ROWS_BETWEEN_FLOORS);

        int x = 3 + rand() % 24;

        // Verificar si hay un bloque sólido en (x, y_suelo)
        if (hay_bloque_en(x, y_suelo)) {
            // Verificar que encima esté libre (donde va la fruta)
            if (!hay_bloque_en(x, y_suelo + 1)) {
                Fruta* f = &juego->frutas.frutas[juego->frutas.cantidad++];
                f->x = x;
                f->y = y_suelo + 1;
                f->tipo = tipos[juego->frutas.cantidad - 1];
                f->activa = 1;
            }
        }

        intentos++;
    }
}
/**
 * Activa el pterodáctilo, que vuela horizontalmente por la parte superior del escenario.
 */
void activar_pterodactilo(Juego* juego) {
    juego->ptero.activo = 1;
    juego->ptero.x = 7;
    juego->ptero.direccion = 1;
    juego->ptero.y = 88;
    printf("Pterodáctilo ACTIVADO en (%d, %d)\n", juego->ptero.x, juego->ptero.y);
}

/**
 * Actualiza la lógica del juego en cada tick:
 * - Aplica física a los jugadores.
 * - Verifica transición de nivel.
 * - Mueve obstáculos y pterodáctilo.
 * - Gestiona colisiones con frutas y enemigos.
 */
void actualizar_juego(Juego* juego, Nivel* mapa) {
    for (int i = 0; i < 2; i++) {
        actualizar_fisica(&juego->jugadores[i]);
    }

    // Calcular nivel actual de cada jugador
    int nivel_popo = obtener_nivel_actual_de_jugador(&juego->jugadores[0]);
    int nivel_nana = obtener_nivel_actual_de_jugador(&juego->jugadores[1]);
    int nivel_mas_alto = (nivel_popo > nivel_nana) ? nivel_popo : nivel_nana;

    // Detectar caída individual con margen visual
    for (int i = 0; i < 2; i++) {
        Jugador* j = &juego->jugadores[i];
        int y_limite = juego->nivel_actual * (TOTAL_FLOOR_HEIGHT + ROWS_BETWEEN_FLOORS);

        if (j->y < y_limite - 2) {
            perder_vida(j);
            int nivel_rescate = nivel_mas_alto;
            int y_base = nivel_rescate * (TOTAL_FLOOR_HEIGHT + ROWS_BETWEEN_FLOORS);
            int nueva_x = 5 + rand() % 20;

            j->x = nueva_x;
            j->y_real = (float)(y_base + 1);
            j->y = y_base + 1;
            j->vy = 0;
            j->en_el_aire = 0;
        }
    }

    // Obtener tiempo actual
    unsigned long ahora = clock() * 1000 / CLOCKS_PER_SEC;

    // Si subieron a un nuevo nivel
    if (nivel_mas_alto > juego->nivel_actual) {
        if (juego->tiempo_subida == 0) {
            juego->tiempo_subida = ahora;  // primer tick registrado
        }

        if (ahora - juego->tiempo_subida >= 2000) {
            for (int nivel = 0; nivel < nivel_mas_alto; nivel++) {
                int y_base = nivel * (TOTAL_FLOOR_HEIGHT + ROWS_BETWEEN_FLOORS);
                for (int offset = -1; offset <= ROWS_BETWEEN_FLOORS; offset++) {
                    vaciar_nivel(y_base + offset);
                }
            }

            juego->nivel_actual = nivel_mas_alto;
            juego->tiempo_subida = 0;

            if (!juego->en_fase_bonus && juego->nivel_actual >= 9) {
                juego->en_fase_bonus = 1;
                for (int i = 0; i < 2; i++) {
                    juego->jugadores[i].vidas++;
                }
                generar_frutas_bonus(juego);
                activar_pterodactilo(juego);
            }
        }
    } else {
        juego->tiempo_subida = 0;
    }

    // Mover obstáculos cada 300 ms
    static unsigned long lastObstacleMove = 0;
    unsigned long now = clock() * 1000 / CLOCKS_PER_SEC;
    if (now - lastObstacleMove >= 300) {
        move_obstacles(juego);
        lastObstacleMove = now;

        // Mover pterodáctilo también aquí
        if (juego->ptero.activo) {
            juego->ptero.x += juego->ptero.direccion;
            if (juego->ptero.x <= 0) juego->ptero.direccion = 1;
            if (juego->ptero.x >= 29) juego->ptero.direccion = -1;
        }
    }

    // Eliminar obstáculos fuera del mapa
    removeObstacleOutOfMap(juego);

    // Verificar colisión con frutas
    for (int i = 0; i < 2; i++) {
        Jugador* j = &juego->jugadores[i];
        for (int f = 0; f < juego->frutas.cantidad; f++) {
            Fruta* fruta = &juego->frutas.frutas[f];
            if (fruta->activa && j->x == fruta->x && j->y == fruta->y) {
                fruta->activa = 0;
                int puntos = 0;
                switch (fruta->tipo) {
                    case NARANJA: puntos = 100; break;
                    case BANANO: puntos = 200; break;
                    case BERENJENA: puntos = 300; break;
                    case LECHUGA: puntos = 400; break;
                }
                sumar_puntaje(j, puntos);
                printf("%s recogió una fruta tipo %d por %d pts\n", j->nombre, fruta->tipo, puntos);
            }
        }
    }

    // Eliminar frutas que quedaron flotando
    for (int f = 0; f < juego->frutas.cantidad; f++) {
        Fruta* fruta = &juego->frutas.frutas[f];
        if (fruta->activa && !hay_bloque_en(fruta->x, fruta->y - 1)) {
            fruta->activa = 0;
            printf("Fruta %d en (%d,%d) eliminada por falta de bloque de soporte\n", fruta->tipo, fruta->x, fruta->y);
        }
    }

    // Verificar colisión con pterodáctilo
    if (juego->ptero.activo) {
        for (int i = 0; i < 2; i++) {
            Jugador* j = &juego->jugadores[i];
            if (j->vidas > 0 && j->x == juego->ptero.x && j->y == juego->ptero.y) {
                j->puntaje += 1000;
                juego->ptero.activo = 0;
                printf("%s atrapó al pterodáctilo y ganó 1000 pts\n", j->nombre);
            }
        }
    }
}

/**
 * Genera un nuevo obstáculo del tipo especificado (YETI, BIRD, ICE_BLOCK) y lo posiciona
 * en el mapa según la lógica del tipo. Luego lo agrega a la lista de obstáculos del juego.
 *
 * @param juego Puntero al estado del juego.
 * @param type Tipo de obstáculo a crear.
 */

void generate_obstacle(Juego* juego, const ObstacleType type) {
    int x = 0;
    int y = 0;

    int DISTANCE_BETWEEN_FLOORS = 6;

    // Definir posición inicial dependiendo del tipo de obstáculo
    switch (type) {
        case YETI:
            // Yeti puede salir por la izquierda o derecha en pisos aleatorios
            const Dir validDirs[] = {LEFT, RIGHT};
            const Dir randomDir = validDirs[rand() % 2];
            x = randomDir == LEFT ? 0 : 30;
            y = FLOOR_HEIGHT + (rand() % 3) * DISTANCE_BETWEEN_FLOORS;
            break;
            // Aves siempre aparecen por la izquierda y en pisos medios
        case BIRD:
            x = 0;
            y = FLOOR_HEIGHT + (rand() % 3) * DISTANCE_BETWEEN_FLOORS;;
            break;
            // Hielos caen desde una posición superior aleatoria
        case ICE_BLOCK:
            x = rand() % 30;
            y = 19;
            break;
    }
    // Crear y agregar obstáculo a la lista
    const Obstacle* obstacle = createObstacle(type, x, y);
    add_obstacle(&juego->obstacles, obstacle);
}
/**
 * Mueve todos los obstáculos activos en el juego, usando su comportamiento específico.
 *
 * @param juego Puntero constante al estado del juego.
 */
void move_obstacles(const Juego* juego) {
    const int size = juego->obstacles.size;
    const ObstacleList* obstacles = &juego->obstacles;

    for (int i = 0; i < size; i++) {
        moveObstacle(&obstacles->obstacles[i]);
    }
}
/**
 * Elimina obstáculos que han salido fuera de los límites del mapa,
 * ya sea horizontal o verticalmente.
 *
 * @param juego Puntero al juego que contiene la lista de obstáculos.
 */
void removeObstacleOutOfMap(Juego* juego) {
    int size = juego->obstacles.size;
    for (int i = 0; i < size; i++) {
        Obstacle obs = juego->obstacles.obstacles[i];
        int x = obs.x;
        int y = obs.y;

        if (!(0 <= x && x <= 30) || !(0 <= y && y <= TOTAL_ROWS)) {
            // delete obstacle
            remove_obstacle(&juego->obstacles, i);
        }
    }
}
/**
 * Imprime por consola todos los obstáculos activos del juego.
 * Útil para depuración y pruebas.
 *
 * @param juego Puntero al estado del juego.
 */
void printObstacles(const Juego* juego) {
    const ObstacleList* obstacles = &juego->obstacles;
    const int size = obstacles->size;

    printf("Game contains %d obstacles\n", size);
    for (int i = 0; i < size; i++) {
        const Obstacle current = obstacles->obstacles[i];
        printf("Type: %d, Pos: (%d, %d), Dir: %d\n", current.type, current.x, current.y, current.dir);
    }
}
#include "acciones.h"
#include "../juego/nivel.h"
#include "../juego/bloque.h"
#include "jugador.h"
#include "constantes.h" // Constantes físicas del juego
#include "acciones.h"
#include "mapa.h"
#include <stdio.h>
#include <math.h>

#include "juego.h"
#include "Obstacle.h"


/**
 * Aplica gravedad y movimiento vertical al jugador,
 * verificando colisiones con bloques y suelo.
 */
void actualizar_fisica(Jugador* j) {
    // Verifica si debería empezar a caer (no hay bloque debajo y no está en el aire)
    int y_abajo = j->y - 1;
    int bloque_abajo = hay_bloque_en(j->x, y_abajo);

    if (!j->en_el_aire && !bloque_abajo) {
        j->en_el_aire = 1;
        j->vy = -0.1f;
        printf("→ Comienza a caer\n");
    }

    if (!j->en_el_aire) return;

    //  Aplicar gravedad
    j->vy += GRAVEDAD;
    if (j->vy < VELOCIDAD_TERMINAL) j->vy = VELOCIDAD_TERMINAL;

    float y_objetivo = j->y_real + j->vy;
    float paso = (j->vy > 0) ? PASO_VERTICAL : -PASO_VERTICAL;

    // Movimiento vertical progresivo, paso a paso
    while ((paso > 0 && j->y_real < y_objetivo) || (paso < 0 && j->y_real > y_objetivo)) {
        j->y_real += paso;
        int y_int = (int)(j->y_real + 0.5f);
        // Límite del mapa
        if (y_int < 0 || y_int >= TOTAL_ROWS) {
            j->y_real = (y_int < 0) ? 0 : TOTAL_ROWS - 1;
            j->y = (int)(j->y_real + 0.5f);
            j->vy = 0;
            j->en_el_aire = 0;
            return;
        }
        // Colisión con un bloque
        if (hay_bloque_en(j->x, y_int)) {
            int bloque_arriba = hay_bloque_en(j->x, y_int + 1);

            if (j->vy < 0) {// Cayendo
                if (!bloque_arriba) {
                    // Aterriza
                    j->y_real = (float)(y_int + 1);
                    j->vy = 0;
                    j->en_el_aire = 0;
                    j->y = y_int + 1;
                    printf("Aterrizó en y=%d (y_real=%.2f)\n", j->y, j->y_real);
                } else {
                    printf("No pudo aterrizar, hay bloque arriba en y=%d\n", y_int + 1);
                }
            } else {
                j->y_real = (float)(y_int - 1);
                j->vy = 0;
                printf("Pegó techo en y=%d\n", y_int);
            }
            break;
        }
    }
    // Actualizar valor entero de y para la lógica del juego
    j->y = (int)(j->y_real + 0.5f);
}


/**
 * Permite al jugador iniciar un salto si está en el suelo.
 */
void brincar_jugador(Jugador* j) {
    printf("Intentando brincar... en_el_aire=%d, bloque_abajo=%d\n",
           j->en_el_aire, hay_bloque_en(j->x, j->y - 1));

    if (!j->en_el_aire && hay_bloque_en(j->x, j->y - 1)) {
        j->vy = FUERZA_SALTO; // Velocidad inicial del salto
        j->en_el_aire = 1;
        printf("%s inició salto con vy=%.2f\n", j->nombre, j->vy);
    } else {
        printf("%s no puede saltar ahora\n", j->nombre);
    }
}

/**
 * Mueve lateralmente al jugador si el camino está libre.
 * 'dir' debe ser 'L' o 'R' (izquierda o derecha).
 * También evita colisiones con obstáculos.
 */
void mover_jugador(Jugador* j, char dir, Juego* juego) {
    int dx = (dir == 'L') ? -1 : 1;
    int nuevo_x = j->x + dx;

    if (nuevo_x >= 0 && nuevo_x < 30 && !hay_bloque_en(nuevo_x, j->y)) {
        // Verificar colisión con obstáculos
        int colision_obstaculo = 0;
        for (int i = 0; i < juego->obstacles.size; i++) {
            Obstacle* o = &juego->obstacles.obstacles[i];
            if (o->x == nuevo_x && o->y == j->y) {
                colision_obstaculo = 1;
                break;
            }
        }
        if (colision_obstaculo) return;  // No se mueve si hay obstáculo

        j->x = nuevo_x;
        j->direccion = dir;
        printf("%s se movió a x=%d\n", j->nombre, j->x);
    }
}

/**
 * Permite destruir un bloque destructible justo encima del jugador,
 * o un obstáculo adyacente (arriba, izquierda, derecha o en su misma posición).
 * Otorga puntaje según el tipo de objeto destruido:
 * - Bloque destructible: 10 pts
 * - ICE_BLOCK: 10 pts
 * - BIRD: 800 pts
 * - YETI: 400 pts
 */
void golpear(Jugador* j, Juego* juego) {
    printf("%s intentó golpear desde (%d, %d)\n", j->nombre, j->x, j->y);

    int puntos = 0;
    int obstaculo_destruido = 0;

    // Buscar bloque destructible justo encima
    Nivel* actual = mapa;
    while (actual) {
        Bloque* b = actual->bloques;
        while (b) {
            if (b->x == j->x && b->y == j->y + 1 && b->activo && b->tipo == 1) {
                b->activo = 0;
            }
            b = b->siguiente;
        }
        actual = actual->siguiente;
    }

    // Buscar obstáculo adyacente (arriba, izquierda, derecha o en misma posición)
    for (int i = 0; i < juego->obstacles.size; i++) {
        Obstacle* o = &juego->obstacles.obstacles[i];
        int dx = abs(o->x - j->x);
        int dy = abs(o->y - j->y);

        if ((dx == 1 && dy == 0) || (dx == 0 && dy == 1) || (dx == 0 && dy == 0)) {
            // Obstáculo está en una celda alcanzable por golpe
            switch (o->type) {
                case ICE_BLOCK:
                    j->puntos_hielo++;
                    sumar_puntaje(j, 10);
                    break;
                case BIRD:
                    j->puntos_ave++;
                    sumar_puntaje(j, 800);
                    break;
                case YETI:
                    j->puntos_yeti++;
                    sumar_puntaje(j, 400);
                    break;
            }
            printf("%s destruyó obstáculo tipo %d en (%d, %d)\n", j->nombre, o->type, o->x, o->y);
            remove_obstacle(&juego->obstacles, i);
            obstaculo_destruido = 1;
            break;
        }
    }

    if (puntos > 0) {
        sumar_puntaje(j, puntos);
    }

    if (puntos == 0 && !obstaculo_destruido) {
        printf("%s golpeó pero no había bloque u obstáculo en la zona\n", j->nombre);
    }
}


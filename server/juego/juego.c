#include "juego.h"
#include "acciones.h"
#include "mapa.h"
#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <time.h>

void inicializar_juego(Juego* juego) {
    juego->nivel_actual = 0;
    juego->en_fase_bonus = 0;
    juego->velocidad = 1;

    Jugador* popo = &juego->jugadores[0];
    Jugador* nana = &juego->jugadores[1];

    snprintf(popo->nombre, sizeof(popo->nombre), "Popo");
    snprintf(nana->nombre, sizeof(nana->nombre), "Nana");

    popo->x = 9;
    popo->y = 1;
    popo->y_real = (float)popo->y;
    popo->vy = 0.0f;
    popo->en_el_aire = 0;
    popo->vidas = 3;
    popo->puntaje = 0;
    popo->direccion = 'R';

    nana->x = 12;
    nana->y = 1;
    nana->y_real = (float)nana->y;
    nana->vy = 0.0f;
    nana->en_el_aire = 0;
    nana->vidas = 3;
    nana->puntaje = 0;
    nana->direccion = 'L';
}

void reiniciar_juego(Juego* juego) {
    juego->nivel_actual = 0;
    juego->en_fase_bonus = 0;
    juego->velocidad++;
}

void imprimir_estado_juego(Juego* juego) {
    printf("Nivel: %d | Velocidad: %d | Bonus: %d\n",
           juego->nivel_actual, juego->velocidad, juego->en_fase_bonus);
}
int obtener_nivel_actual_de_jugador(Jugador* j) {
    int mejor_y = -1;
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


void actualizar_juego(Juego* juego, Nivel* mapa) {
    int niveles_previos = juego->nivel_actual;

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

        if (j->y < y_limite - 2) {  // -2 para permitir que baje unos cuadros
            perder_vida(j);

            // Reaparecer en el último nivel alcanzado (más alto de ambos)
            int nivel_rescate = nivel_mas_alto;
            int y_base = nivel_rescate * (TOTAL_FLOOR_HEIGHT + ROWS_BETWEEN_FLOORS);

            // Escoge una nueva posición x (centrado aleatorio entre 5 y 24)
            int nueva_x = 5 + rand() % 20;

            j->x = nueva_x;
            j->y_real = (float)(y_base + 1);
            j->y = y_base + 1;
            j->vy = 0;
            j->en_el_aire = 0;

            printf("%s cayó al vacío. Reaparece en nivel %d, x=%d\n", j->nombre, nivel_rescate, j->x);
        }
    }

    // Si subieron a un nuevo nivel
    if (nivel_mas_alto > juego->nivel_actual) {
        for (int nivel = 0; nivel < nivel_mas_alto; nivel++) {
            int y_base = nivel * (TOTAL_FLOOR_HEIGHT + ROWS_BETWEEN_FLOORS);
            for (int offset = -1; offset <= ROWS_BETWEEN_FLOORS; offset++) {
                vaciar_nivel(y_base + offset);
            }
        }

        juego->nivel_actual = nivel_mas_alto;

        if (!juego->en_fase_bonus && juego->nivel_actual >= 9) {
            juego->en_fase_bonus = 1;
            for (int i = 0; i < 2; i++) {
                juego->jugadores[i].vidas++;
            }
            printf("¡Fase BONUS iniciada! +1 vida\n");
        }
    }
}

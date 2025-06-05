#include "juego.h"
#include "acciones.h"
#include "mapa.h"
#include <stdio.h>
#include <stdlib.h>
#include <math.h>

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
    popo->vidas = 3;
    popo->puntaje = 0;
    popo->direccion = 'R';

    nana->x = 12;
    nana->y = 1;
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
    // Primero intenta detectar si hay un bloque justo debajo o cerca
    for (int offset = 0; offset <= 6; offset++) {
        int y_check = j->y - offset;
        if (y_check >= 0 && hay_bloque_en(j->x, y_check)) {
            return y_check / (TOTAL_FLOOR_HEIGHT + ROWS_BETWEEN_FLOORS);
        }
    }

    // Como fallback, usa la altura estimada
    return j->y / (TOTAL_FLOOR_HEIGHT + ROWS_BETWEEN_FLOORS);
}


void actualizar_juego(Juego* juego, Nivel* mapa) {
    for (int i = 0; i < 2; i++) {
        actualizar_fisica(&juego->jugadores[i]);
    }

    // Verifica separación vertical entre jugadores
    int y0 = juego->jugadores[0].y;
    int y1 = juego->jugadores[1].y;

    int abajo = (y0 > y1) ? 0 : 1;
    int arriba = 1 - abajo;

    Jugador* j_abajo = &juego->jugadores[abajo];
    Jugador* j_arriba = &juego->jugadores[arriba];

    if (abs(y0 - y1) > 2) {
        // Solo castigar si el de abajo no está en el aire
        if (!j_abajo->en_el_aire) {
            perder_vida(j_abajo);
            j_abajo->y = j_arriba->y;
            printf("%s estaba muy abajo. Vida menos y reposicionado\n", j_abajo->nombre);
        }
    }

    // Avanza nivel si ambos llegaron arriba
    if (!juego->en_fase_bonus && juego->nivel_actual < 31) {
        if (juego->jugadores[0].y <= 1 && juego->jugadores[1].y <= 1) {
            juego->nivel_actual++;
        }
    }

    // Inicia fase bonus al llegar al último nivel
    if (juego->nivel_actual == 31 && !juego->en_fase_bonus) {
        juego->en_fase_bonus = 1;
        for (int i = 0; i < 2; i++) {
            juego->jugadores[i].vidas++;
        }
        reiniciar_juego(juego);
        printf("¡BONUS! +1 vida y reinicio\n");
    }

    // Actualiza el nivel actual real según altura de los jugadores
    int nivel_popo = obtener_nivel_actual_de_jugador(&juego->jugadores[0]);
    int nivel_nana = obtener_nivel_actual_de_jugador(&juego->jugadores[1]);
    int nivel_mas_alto = (nivel_popo > nivel_nana) ? nivel_popo : nivel_nana;
    juego->nivel_actual = nivel_mas_alto;
}




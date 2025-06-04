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

void actualizar_juego(Juego* juego, Nivel* mapa) {
    // Aplica gravedad a ambos jugadores
    for (int i = 0; i < 2; i++) {
        Jugador* j = &juego->jugadores[i];
        if (j->saltando > 0) {
            int y_arriba = j->y + 1;
            if (y_arriba < TOTAL_ROWS && !hay_bloque_en(j->x, y_arriba)) {
                j->y = y_arriba;
                printf("%s está saltando a y=%d\n", j->nombre, j->y);
            }
            j->saltando--;  // disminuye cada frame
        } else {
            aplicar_gravedad(j);  // solo si no está saltando
        }
    }


    // Verifica separación vertical entre jugadores
    int y0 = juego->jugadores[0].y;
    int y1 = juego->jugadores[1].y;
    if (abs(y0 - y1) > 2) {
        int abajo = (y0 > y1) ? 0 : 1;
        perder_vida(&juego->jugadores[abajo]);
        juego->jugadores[abajo].y = juego->jugadores[1 - abajo].y;
        printf("%s estaba muy abajo. Vida menos y reposicionado\n", juego->jugadores[abajo].nombre);
    }

    // Avanza nivel si ambos llegaron arriba
    if (!juego->en_fase_bonus && juego->nivel_actual < 31) {
        if (juego->jugadores[0].y <= 1 && juego->jugadores[1].y <= 1) {
            juego->nivel_actual++;
            printf("Avanzando al nivel %d\n", juego->nivel_actual);
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
    int nivel_popo = juego->jugadores[0].y / (TOTAL_FLOOR_HEIGHT + ROWS_BETWEEN_FLOORS);
    int nivel_nana = juego->jugadores[1].y / (TOTAL_FLOOR_HEIGHT + ROWS_BETWEEN_FLOORS);
    int nivel_mas_alto = (nivel_popo > nivel_nana) ? nivel_popo : nivel_nana;
    juego->nivel_actual = nivel_mas_alto;
}


#include "juego.h"
#include <stdio.h>
#include <string.h>

void inicializar_juego(Juego *juego) {
    memset(juego, 0, sizeof(Juego));
    strcpy(juego->jugadores[0].nombre, "Popo");
    strcpy(juego->jugadores[1].nombre, "Nana");
    juego->jugadores[0].vidas = juego->jugadores[1].vidas = 3;
    juego->velocidad = 1;
}

void actualizar_juego(Juego *juego) {
    // Placeholder: lógica para mover obstáculos, verificar colisiones, etc.
}

void reiniciar_juego(Juego *juego) {
    juego->nivel_actual = 0;
    juego->en_fase_bonus = 0;
    juego->velocidad += 1;
}

void imprimir_estado_juego(Juego *juego) {
    printf("Nivel: %d | Velocidad: %d | Bonus: %d\n",
           juego->nivel_actual, juego->velocidad, juego->en_fase_bonus);
}

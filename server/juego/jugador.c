#include "jugador.h"
#include <stdio.h>

void mover_jugador(Jugador *jugador, char direccion) {
    jugador->direccion = direccion;
    if (direccion == 'L') jugador->x -= 1;
    if (direccion == 'R') jugador->x += 1;
}

void golpear(Jugador *jugador) {
    printf("%s golpea con el martillo!\n", jugador->nombre);
}

void perder_vida(Jugador *jugador) {
    if (jugador->vidas > 0) jugador->vidas--;
}

#include "jugador.h"
#include <stdio.h>

void perder_vida(Jugador* j) {
    if (j->vidas > 0) {
        j->vidas--;
        printf("%s perdió una vida. Vidas restantes: %d\n", j->nombre, j->vidas);
    }
}

void sumar_puntaje(Jugador* j, int valor) {
    j->puntaje += valor;
    printf("%s ganó %d puntos. Total: %d\n", j->nombre, valor, j->puntaje);
}

#include "jugador.h"
#include <stdio.h>
/**
 * Resta una vida al jugador, si aún tiene vidas disponibles.
 *
 * @param j Puntero al jugador que pierde una vida.
 */
void perder_vida(Jugador* j) {
    if (j->vidas > 0) {
        j->vidas--;
        //printf("%s perdió una vida. Vidas restantes: %d\n", j->nombre, j->vidas);
    }
}
/**
 * Suma puntos al puntaje total del jugador.
 *
 * @param j Puntero al jugador que gana puntos.
 * @param valor Cantidad de puntos a sumar.
 */
void sumar_puntaje(Jugador* j, int valor) {
    j->puntaje += valor;
    //printf("%s ganó %d puntos. Total: %d\n", j->nombre, valor, j->puntaje);
}

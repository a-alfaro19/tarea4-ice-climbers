// acciones.h
#ifndef ACCIONES_H
#define ACCIONES_H

#include "../juego/nivel.h"
#include "../juego/bloque.h"
#include "jugador.h"


/**
 * Mueve al jugador en la dirección indicada.
 * @param j puntero al jugador.
 * @param dir 'L' o 'R'.
 */
void mover_jugador(Jugador* j, char dir);

/**
 * Hace que el jugador suba una posición.
 */
void brincar_jugador(Jugador* j);

/**
 * Permite al jugador destruir un bloque si está arriba.
 */
void golpear(Jugador* j, Nivel* nivel);

#endif

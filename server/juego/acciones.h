#ifndef ACCIONES_H
#define ACCIONES_H

#include "../juego/nivel.h"
#include "../juego/bloque.h"
#include "jugador.h"

/**
 * Procesa física del jugador: salto (vy > 0) o gravedad (caída).
 */
void actualizar_fisica(Jugador* j);

/**
 * Mueve al jugador lateralmente si el camino está libre.
 */
void mover_jugador(Jugador* j, char dir);

/**
 * Inicia un salto si el jugador no está en el aire.
 */
void brincar_jugador(Jugador* j, Nivel* mapa);

/**
 * Permite destruir un bloque destructible justo encima.
 */
void golpear(Jugador* j, Nivel* nivel);

#endif

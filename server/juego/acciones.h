// acciones.h
#ifndef ACCIONES_H
#define ACCIONES_H

#include "../juego/nivel.h"
#include "../juego/bloque.h"
#include "jugador.h"

/**
 * Aplica gravedad al jugador, haciéndolo caer hasta el siguiente bloque debajo.
 * Si no hay piso, cae hasta el fondo.
 */
void aplicar_gravedad(Jugador* j);

/**
 * Mueve al jugador en la dirección indicada ('L' = izquierda, 'R' = derecha),
 * si no hay un bloque sólido en esa dirección. Aplica gravedad tras moverse.
 */
void mover_jugador(Jugador* j, char dir);

/**
 * Hace que el jugador suba una posición si el espacio está libre.
 * Luego aplica gravedad automáticamente.
 */
void brincar_jugador(Jugador* j, Nivel* mapa);

/**
 * Permite al jugador destruir un bloque destructible (tipo 1) justo arriba,
 * si existe y está activo.
 */
void golpear(Jugador* j, Nivel* nivel);

#endif

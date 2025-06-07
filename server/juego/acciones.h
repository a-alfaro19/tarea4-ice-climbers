#ifndef ACCIONES_H
#define ACCIONES_H
#include "jugador.h"
#include "../juego/nivel.h"

/**
 * Aplica la física vertical al jugador: gravedad si está cayendo o salto si está en el aire.
 * También se encarga de detectar colisiones verticales con bloques.
 *
 * @param j Puntero al jugador que se va a actualizar.
 */
void actualizar_fisica(Jugador* j);

/**
 * Mueve al jugador lateralmente hacia la izquierda o derecha,
 * si no hay bloque sólido en la dirección deseada.
 *
 * @param j Puntero al jugador que se va a mover.
 * @param dir Dirección de movimiento: 'L' para izquierda, 'R' para derecha.
 */
void mover_jugador(Jugador* j, char dir);

/**
 * Inicia un salto si el jugador está en el suelo (no en el aire)
 * y hay un bloque sólido debajo que lo soporte.
 *
 * @param j Puntero al jugador que intenta brincar.
 */
void brincar_jugador(Jugador* j);
/**
 * Intenta destruir un bloque destructible (tipo 1) justo arriba del jugador.
 * Si lo encuentra, lo desactiva (lo elimina del juego visualmente).
 *
 * @param j Puntero al jugador que realiza la acción de golpear.
 * @param nivel No se utiliza (puede usarse para refactorización futura).
 */
void golpear(Jugador* j, Nivel* nivel);

#endif

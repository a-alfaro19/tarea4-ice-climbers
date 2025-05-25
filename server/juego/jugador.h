#ifndef JUGADOR_H
#define JUGADOR_H

#include "../util/tipos.h"

void mover_jugador(Jugador *jugador, char direccion);
void golpear(Jugador *jugador);
void perder_vida(Jugador *jugador);

#endif

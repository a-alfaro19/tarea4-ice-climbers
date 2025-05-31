#ifndef JUGADOR_H
#define JUGADOR_H

#include "../util/tipos.h"
#include <stdio.h>
#include <string.h>
#include "../util/constantes.h"
#include "nivel.h"

void mover_jugador(Jugador *jugador, char direccion);
void golpear(Jugador *jugador, Nivel *nivel);
void perder_vida(Jugador *jugador);
void brincar_jugador(Jugador *jugador);
void sumar_puntaje(Jugador *jugador, int puntos);

#endif

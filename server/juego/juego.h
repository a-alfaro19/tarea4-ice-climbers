#ifndef JUEGO_H
#define JUEGO_H

#include "../util/tipos.h"
#include "obstaculo.h"
#include "jugador.h"
#include "nivel.h"
#include "../util/constantes.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>


void inicializar_juego(Juego *juego);
void actualizar_juego(Juego *juego);
void reiniciar_juego(Juego *juego);
void imprimir_estado_juego(Juego *juego);

#endif

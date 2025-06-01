#ifndef JUEGO_H
#define JUEGO_H

#include "../util/tipos.h"
#include "nivel.h"
#include "jugador.h"
#include <string.h>
#include <stddef.h>
#include <stdlib.h>
#include <stdio.h>

struct Juego {
    int nivel_actual;
    int velocidad;
    int en_fase_bonus;
    Nivel niveles[MAX_NIVELES];
    Jugador jugadores[2];
};

void inicializar_juego(Juego *juego);
void actualizar_juego(Juego *juego);
void reiniciar_juego(Juego *juego);
void imprimir_estado_juego(Juego *juego);

#endif

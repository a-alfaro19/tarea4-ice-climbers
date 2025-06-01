#ifndef JUGADOR_H
#define JUGADOR_H

#include "../util/tipos.h"
#include "nivel.h"

struct Jugador {
    char nombre[20];
    int x, y;
    int puntaje;
    int vidas;
    char direccion;
};

void mover_jugador(Jugador *jugador, char direccion);
void golpear(Jugador *jugador, Nivel *nivel);
void perder_vida(Jugador *jugador);
void brincar_jugador(Jugador *jugador);
void sumar_puntaje(Jugador *jugador, int puntos);

#endif


#ifndef JUEGO_H
#define JUEGO_H

#include "nivel.h"
#include "jugador.h"

typedef struct {
    Jugador jugadores[2];  // Popo y Nana
    int nivel_actual;      // Índice actual (0 a 31)
    int en_fase_bonus;     // 1 = sí, 0 = no
    int velocidad;         // Para futuras animaciones o lógica
} Juego;

/**
 * Inicializa el estado del juego: jugadores, nivel, velocidad.
 */
void inicializar_juego(Juego* juego);

/**
 * Reinicia el juego (sube velocidad, reinicia nivel).
 */
void reiniciar_juego(Juego* juego);

/**
 * Imprime el estado actual del juego (debug).
 */
void imprimir_estado_juego(Juego* juego);

/**
 * Avanza la lógica del juego (nivel, puntaje, vidas, etc.).
 */
void actualizar_juego(Juego* juego, Nivel* mapa);

#endif

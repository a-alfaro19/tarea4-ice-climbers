#ifndef JUEGO_H
#define JUEGO_H

#define MAX_OBSTACLES 4

#include "nivel.h"
#include "jugador.h"
#include "ObstacleList.h"

typedef struct {
    Jugador jugadores[2];  // Popo y Nana
    ObstacleList obstacles; // Obstacles list
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

int obtener_nivel_actual_de_jugador(Jugador* j);

/**
 * Avanza la lógica del juego (nivel, puntaje, vidas, etc.).
 */
void actualizar_juego(Juego* juego, Nivel* mapa);

void generate_obstacle(Juego* juego, ObstacleType type);

void move_obstacles(const Juego* juego);

void printObstacles(const Juego* juego);

#endif

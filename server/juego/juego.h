#ifndef JUEGO_H
#define JUEGO_H

#include "nivel.h"
#include "jugador.h"
#include "ObstacleList.h"
#include "fruta.h"

/**
 * Representa al pterodáctilo.
 */
typedef struct {
    int activo; // 1 si está presente en el juego, 0 si no
    int x;
    int y;
    int direccion;  // 1 = derecha, -1 = izquierda
} Pterodactilo;

/**
 * Estructura principal que representa el estado global del juego.
 */
typedef struct {
    Jugador jugadores[2];      // Arreglo de jugadores (Popo y Nana)
    ObstacleList obstacles;    // Lista dinámica de obstáculos (Yeti, ave, hielo)
    ListaFrutas frutas;        // Frutas en el mapa (fase bonus)
    int nivel_actual;          // Índice del nivel más alto alcanzado
    int en_fase_bonus;         // 1 si está en fase bonus, 0 si no
    int velocidad;             // Velocidad del juego (aumenta en cada bonus)
    Pterodactilo ptero;        // Estado del pterodáctilo
    int atrapo_ptero; // 1 si colisionó con el pterodáctilo, 0 si no
    unsigned long tiempo_subida; // Tiempo desde que se alcanzó nuevo nivel (para transición)
} Juego;
/**
 * Inicializa el estado del juego: jugadores, nivel, velocidad, pterodáctilo.
 *
 * @param juego Puntero a la estructura de juego a inicializar.
 */
void inicializar_juego(Juego* juego);

/**
 * Reinicia la partida actual, aumentando la velocidad y reiniciando el nivel.
 *
 * @param juego Puntero a la estructura de juego.
 */
void reiniciar_juego(Juego* juego);

/**
 * Imprime en consola el estado actual del juego (nivel, velocidad, fase bonus).
 *
 * @param juego Puntero al juego del cual se quiere imprimir el estado.
 */
void imprimir_estado_juego(Juego* juego);
/**
 * Calcula y retorna el nivel actual en que se encuentra el jugador,
 * basándose en su posición vertical.
 *
 * @param j Puntero al jugador.
 * @return Índice del nivel correspondiente.
 */
int obtener_nivel_actual_de_jugador(Jugador* j);

/**
 * Actualiza el estado del juego: físicas, colisiones, bonus, obstáculos, frutas.
 *
 * @param juego Puntero al juego que se desea actualizar.
 * @param mapa Referencia al mapa donde están los niveles y bloques.
 */
void actualizar_juego(Juego* juego, Nivel* mapa);

/**
 * Crea y agrega un nuevo obstáculo al juego (Yeti, ave o hielo).
 *
 * @param juego Puntero al juego.
 * @param type Tipo de obstáculo a generar.
 */
void generate_obstacle(Juego* juego, ObstacleType type);
/**
 * Mueve todos los obstáculos activos en el juego (según su tipo y dirección).
 *
 * @param juego Puntero constante al juego.
 */
void move_obstacles(const Juego* juego);
/**
 * Imprime en consola todos los obstáculos activos (para depuración).
 *
 * @param juego Puntero al juego.
 */
void printObstacles(const Juego* juego);
/**
 * Elimina del juego los obstáculos que hayan salido fuera del mapa.
 *
 * @param juego Puntero al juego.
 */
void removeObstacleOutOfMap(Juego* juego);
/**
 * Activa el pterodáctilo en la parte superior del mapa (fase bonus).
 *
 * @param juego Puntero al juego.
 */
void activar_pterodactilo(Juego* juego);
#endif

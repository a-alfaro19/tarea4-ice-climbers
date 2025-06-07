#ifndef OBSTACLELIST_H
#define OBSTACLELIST_H
#include "Obstacle.h"

/**
 * Representa una lista dinámica de obstáculos activos en el juego.
 * Se usa para almacenar Yeti, aves y bloques de hielo generados durante la partida.
 */
typedef struct {
    Obstacle* obstacles; // Arreglo dinámico de obstáculos
    int size; // Cantidad actual de obstáculos en la lista
} ObstacleList;
/**
 * Inicializa una nueva lista vacía de obstáculos.
 *
 * @return Puntero a la nueva lista inicializada.
 */
ObstacleList* initialize_obstacle_list();
/**
 * Agrega un nuevo obstáculo al final de la lista.
 * La lista se redimensiona dinámicamente si es necesario.
 *
 * @param obs_list Puntero a la lista donde se agregará.
 * @param obstacle Puntero al obstáculo que se quiere agregar.
 */
void add_obstacle(ObstacleList* obs_list, const Obstacle* obstacle);
/**
 * Elimina el obstáculo en la posición indicada de la lista.
 * No redimensiona el arreglo, solo reduce el tamaño lógico.
 *
 * @param obs_list Puntero a la lista.
 * @param index Índice del obstáculo a eliminar.
 */
void remove_obstacle(ObstacleList* obs_list, int index);

#endif //OBSTACLELIST_H

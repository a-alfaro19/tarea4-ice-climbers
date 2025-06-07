#include "ObstacleList.h"
#include <stdio.h>
#include <stdlib.h>

/**
 * Crea e inicializa una lista vacía de obstáculos.
 * Se debe liberar externamente si ya no se utiliza.
 */
ObstacleList* initialize_obstacle_list() {
    ObstacleList* obs_list = malloc(sizeof(ObstacleList));
    obs_list->size = 0;
    obs_list->obstacles = NULL;
    return obs_list;
}

/**
 * Agrega un obstáculo a la lista dinámica.
 * Si es la primera vez, se aloca memoria; si ya hay elementos, se redimensiona con realloc.
 * En caso de error de memoria, el programa se termina.
 */
void add_obstacle(ObstacleList* obs_list, const Obstacle* obstacle) {
    const int size = obs_list->size;
    obs_list->obstacles = realloc(obs_list->obstacles, (size + 1) * sizeof(Obstacle));
    // Redimensionar arreglo
    if (obs_list->obstacles == NULL) {
        printf("Memory error\n");
        exit(1);
    }
    // Copiar obstáculo al final
    obs_list->obstacles[size] = *obstacle;
    obs_list->size++;
}
/**
 * Elimina el obstáculo en la posición especificada desplazando el resto hacia la izquierda.
 * No libera memoria del arreglo (puedes hacerlo si deseas optimización extra).
 */
void remove_obstacle(ObstacleList* obs_list, const int index) {
    const int size = obs_list->size;
    if (index < 0 || index >= size) return;
    // Sobrescribir el obstáculo con los siguientes
    for (int i = index; i < size; i++) {
        obs_list->obstacles[i] = obs_list->obstacles[i + 1];
    }

    obs_list->size--;
    // obs_list->obstacles = realloc(obs_list->obstacles, obs_list->size * sizeof(Obstacle));
}

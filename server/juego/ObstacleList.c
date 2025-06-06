#include "ObstacleList.h"

#include <stdio.h>
#include <stdlib.h>

ObstacleList* initialize_obstacle_list() {
    ObstacleList* obs_list = malloc(sizeof(ObstacleList));
    obs_list->size = 0;
    obs_list->obstacles = NULL;
    return obs_list;
}

void add_obstacle(ObstacleList* obs_list, const Obstacle* obstacle) {
    const int size = obs_list->size;
    obs_list->obstacles = realloc(obs_list->obstacles, (size + 1) * sizeof(Obstacle));

    if (obs_list->obstacles == NULL) {
        printf("Memory error\n");
        exit(1);
    }
    obs_list->obstacles[size] = *obstacle;
    obs_list->size++;
}

void remove_obstacle(ObstacleList* obs_list, const int index) {
    const int size = obs_list->size;
    if (index < 0 || index >= size) return;

    for (int i = index; i < size; i++) {
        obs_list->obstacles[i] = obs_list->obstacles[i + 1];
    }

    obs_list->size--;
    // obs_list->obstacles = realloc(obs_list->obstacles, obs_list->size * sizeof(Obstacle));
}

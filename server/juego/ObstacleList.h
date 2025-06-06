#ifndef OBSTACLELIST_H
#define OBSTACLELIST_H

#include "Obstacle.h"

typedef struct {
    Obstacle* obstacles;
    int size;
} ObstacleList;

ObstacleList* initialize_obstacle_list();
// void destroy_obstacle_list();
void add_obstacle(ObstacleList* obs_list, const Obstacle* obstacle);
// void remove_obstacle(Obstacle* obstacle);
void remove_obstacle(ObstacleList* obs_list, int index);

#endif //OBSTACLELIST_H

#include "Obstacle.h"
#include <stdlib.h>
#include <string.h>


Obstacle* createObstacle(const ObstacleType type, const int x, const int y) {
    Obstacle* obstacle = malloc(sizeof(Obstacle));

    // Set Dir
    switch (type) {
        case YETI:
            obstacle->dir = x == 0 ? RIGHT : LEFT;
            break;

        case BIRD:
            obstacle->dir = RIGHT;
            break;

        case ICE_BLOCK:
            obstacle->dir = DOWN;
            break;
    }

    obstacle->type = type;
    obstacle->x = x;
    obstacle->y = y;

    return obstacle;
}

void moveObstacle(Obstacle* obstacle) {
    switch (obstacle->type) {
        case YETI:
            // Always moves horizontally
            obstacle->x += obstacle->dir == RIGHT ? 1 : -1;
            break;

        case BIRD:
            // Set new Y
            const Dir validDirs[] = {UP, DOWN};
            const int dirArraySize = sizeof(validDirs) / sizeof(Dir);
            const Dir randomDir = validDirs[rand() % dirArraySize];
            obstacle->y += randomDir == UP ? -1 : 1;

            // Set new X
            obstacle->x += 1;
            break;

        case ICE_BLOCK:
            // Always moves down
            obstacle->y += 1;
            break;
    }
}

ObstacleType parse_obstacle_type(char* typeStr) {
    if (strcmp(typeStr, "yeti") == 0) return YETI;
    if (strcmp(typeStr, "bird") == 0) return BIRD;
    if (strcmp(typeStr, "ice_block") == 0) return ICE_BLOCK;
    return YETI;
}


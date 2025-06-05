#ifndef OBSTACLE_H
#define OBSTACLE_H

typedef enum {
    YETI,
    BIRD,
    ICE_BLOCK
} ObstacleType;

typedef enum {
    LEFT,
    RIGHT,
    UP,
    DOWN
} Dir;

typedef struct {
    ObstacleType type;
    int lives;
    int x, y;
    Dir dir;
} Obstacle;

Obstacle* createObstacle(ObstacleType type, int x, int y);

void moveObstacle(Obstacle* obstacle);

#endif //OBSTACLE_H

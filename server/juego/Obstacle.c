#include "Obstacle.h"
#include <stdlib.h>
#include <string.h>

/**
 * Crea un nuevo obstáculo con posición y tipo especificado.
 * Asigna la dirección inicial basada en su tipo y posición.
 */
Obstacle* createObstacle(const ObstacleType type, const int x, const int y) {
    Obstacle* obstacle = malloc(sizeof(Obstacle));

    // Asignar dirección inicial según tipo
    switch (type) {
        case YETI:
            obstacle->dir = x == 0 ? RIGHT : LEFT;  // sale por izquierda o derecha
            break;

        case BIRD:
            obstacle->dir = RIGHT; // aves siempre vuelan hacia la derecha
            break;

        case ICE_BLOCK:
            obstacle->dir = DOWN; // hielo cae en línea recta
            break;
    }

    obstacle->type = type;
    obstacle->x = x;
    obstacle->y = y;

    return obstacle;
}
/**
 * Actualiza la posición del obstáculo de acuerdo a su tipo y comportamiento:
 * - YETI: camina en línea horizontal.
 * - BIRD: vuela hacia la derecha con cambio vertical aleatorio (sube o baja).
 * - ICE_BLOCK: cae hacia abajo.
 */
void moveObstacle(Obstacle* obstacle) {
    switch (obstacle->type) {
        case YETI:
            // Siempre se mueve horizontal
            obstacle->x += obstacle->dir == RIGHT ? 1 : -1;
            break;

        case BIRD:
            // Movimiento vertical aleatorio (sube o baja)
            const Dir validDirs[] = {UP, DOWN};
            const int dirArraySize = sizeof(validDirs) / sizeof(Dir);
            const Dir randomDir = validDirs[rand() % dirArraySize];
            obstacle->y += randomDir == UP ? -1 : 1;


            obstacle->x += 1;
            break;

        case ICE_BLOCK:
            // Always moves down
            obstacle->y -= 1;
            break;
    }
}
/**
 * Convierte una cadena como "yeti", "bird", o "ice_block" en su equivalente enum.
 */
ObstacleType parse_obstacle_type(char* typeStr) {
    if (strcmp(typeStr, "yeti") == 0) return YETI;
    if (strcmp(typeStr, "bird") == 0) return BIRD;
    if (strcmp(typeStr, "ice_block") == 0) return ICE_BLOCK;
    return YETI;
}


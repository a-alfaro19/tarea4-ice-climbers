#ifndef OBSTACLE_H
#define OBSTACLE_H

/**
 * Tipos de obstáculos disponibles en el juego.
 */
typedef enum {
    YETI,       // Enemigo que camina horizontalmente desde un borde
    BIRD,       // Aves que se mueven diagonalmente y pueden cambiar de nivel
    ICE_BLOCK   // Hielo que cae en línea recta desde arriba
} ObstacleType;

/**
 * Direcciones posibles para el movimiento de los obstáculos.
 */
typedef enum {
    LEFT,
    RIGHT,
    UP,
    DOWN
} Dir;

/**
 * Representa un obstáculo en el juego (Yeti, Ave o Hielo).
 */
typedef struct {
    ObstacleType type; // Tipo de obstáculo
    int lives;         // (No usado actualmente, reservado para futuras mejoras)
    int x, y;          // Posición del obstáculo en el mapa
    Dir dir;           // Dirección actual del movimiento
} Obstacle;
/**
 * Crea un obstáculo del tipo y posición indicados, asignando dirección automáticamente.
 *
 * @param type Tipo de obstáculo (YETI, BIRD, ICE_BLOCK)
 * @param x Coordenada horizontal inicial
 * @param y Coordenada vertical inicial
 * @return Puntero al nuevo obstáculo
 */
Obstacle* createObstacle(ObstacleType type, int x, int y);
/**
 * Mueve el obstáculo una unidad en su dirección correspondiente,
 * aplicando reglas específicas según su tipo.
 *
 * @param obstacle Puntero al obstáculo que se va a mover.
 */
void moveObstacle(Obstacle* obstacle);
/**
 * Convierte una cadena como "yeti", "bird" o "ice_block" al enum correspondiente.
 *
 * @param typeStr Cadena de texto que representa el tipo de obstáculo.
 * @return Valor del tipo de obstáculo.
 */
ObstacleType parse_obstacle_type(char* typeStr);

#endif //OBSTACLE_H

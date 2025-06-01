#ifndef OBSTACULO_H
#define OBSTACULO_H
#include "nivel.h"
#define MAX_OBSTACULOS 20


// Enumeración de los tipos de obstáculos del juego
typedef enum {
    YETI,
    AVE,
    BLOQUE_HIELO
} TipoObstaculo;

// Estructura para manejar la posición en 2D
typedef struct {
    int x, y;
} Pos;

// Estructura para manejar las dimensiones de la figura
typedef struct {
    int ancho, alto;
} Dimensiones;

// Enumeración de las direcciones de movimiento
typedef enum {
    ARRIBA,
    ABAJO,
    IZQ,
    DER
} Direccion;

// Estructura principal del obstáculo
typedef struct Obstaculo_St {
    TipoObstaculo tipo; // Tipo de obstáculo
    Pos pos; // Posición actual
    Direccion direccion; // Dirección en la que se moverá el obstáculo
    Dimensiones dimensiones; // Dimensiones del obstáculo
    int activo; // Estado del obstáculo

    // Funciones de comportamiento
    void (*mover)(struct Obstaculo_St*);
    void (*dibujar)(struct Obstaculo_St*);
} Obstaculo;

/**
 * Crea un obstáculo del tipo especificado.
 * @param tipo Tipo del obstáculo a crear.
 * @param x Posición inicial en X.
 * @param y Posición inicial en Y.
 * @param ancho Ancho del obstáculo.
 * @param alto Alto del obstáculo.
 * @return Puntero al nuevo Obstáculo creado.
 */
Obstaculo* crear_obstaculo(TipoObstaculo tipo, int x, int y, int ancho, int alto);

/**
 * Libera la memoria ocupada por un obstáculo.
 * @param obs Puntero al obstáculo a destruir.
 * @return 1 si la operación fue exitosa, 0 para el caso contrario.
 */
int destruir_obstaculo(const Obstaculo* obs);
/**
 * Crea un obstáculo y lo agrega al nivel especificado.
 * @param nivel Nivel al que se desea agregar el obstáculo.
 * @param tipo Tipo de obstáculo (YETI, AVE, BLOQUE_HIELO).
 * @param x Coordenada X.
 * @param y Coordenada Y.
 * @return 1 si se agregó correctamente, 0 si hubo error.
 */
int crear_y_agregar_obstaculo(Nivel *nivel, TipoObstaculo tipo, int x, int y);

#endif

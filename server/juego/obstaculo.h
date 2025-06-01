#ifndef OBSTACULO_H
#define OBSTACULO_H

#include "../util/tipos.h"
#include "../util/constantes.h"
#include "nivel.h"

typedef enum {
    YETI,
    AVE,
    BLOQUE_HIELO
} TipoObstaculo;

typedef enum {
    ARRIBA,
    ABAJO,
    IZQ,
    DER
} Direccion;

typedef struct {
    int x, y;
} Pos;

typedef struct {
    int ancho, alto;
} Dimensiones;

struct Obstaculo {
    TipoObstaculo tipo;
    Pos pos;
    Direccion direccion;
    Dimensiones dimensiones;
    int activo;
    void (*mover)(struct Obstaculo*);
    void (*dibujar)(struct Obstaculo*);
};

Obstaculo* crear_obstaculo(TipoObstaculo tipo, int x, int y, int ancho, int alto);
int destruir_obstaculo(const Obstaculo* obs);
int crear_y_agregar_obstaculo(Nivel *nivel, TipoObstaculo tipo, int x, int y);
static void dibujar_obstaculo(Obstaculo* obs);



#endif

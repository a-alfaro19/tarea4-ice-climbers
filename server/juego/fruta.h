#ifndef FRUTA_H
#define FRUTA_H

typedef enum {
    NARANJA = 0,
    BANANO,
    BERENJENA,
    LECHUGA
} TipoFruta;

typedef struct {
    int x, y;
    TipoFruta tipo;
    int activa;
} Fruta;

#define MAX_FRUTAS 4

typedef struct {
    Fruta frutas[MAX_FRUTAS];
    int cantidad;
} ListaFrutas;

#endif

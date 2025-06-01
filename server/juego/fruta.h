#ifndef FRUTA_H
#define FRUTA_H

#include "../util/tipos.h"

struct Fruta {
    char tipo[20];
    int x, y;
    int valor;
    int recolectada;
};

Fruta crear_fruta(const char *tipo, int x, int y);
int obtener_valor_fruta(const char *tipo);

#endif

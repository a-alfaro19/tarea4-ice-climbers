#include "fruta.h"
#include <string.h>

int obtener_valor_fruta(const char *tipo) {
    if (strcmp(tipo, "naranja") == 0) return 100;
    if (strcmp(tipo, "banana") == 0) return 200;
    if (strcmp(tipo, "berenjena") == 0) return 300;
    if (strcmp(tipo, "lechuga") == 0) return 400;
    return 0;
}

Fruta crear_fruta(const char *tipo, int x, int y) {
    Fruta f;
    strcpy(f.tipo, tipo);
    f.valor = obtener_valor_fruta(tipo);
    f.x = x;
    f.y = y;
    f.recolectada = 0;
    return f;
}

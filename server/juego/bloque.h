#ifndef BLOQUE_H
#define BLOQUE_H

typedef struct Bloque {
    int x, y;           // coordenadas
    int tipo;           // 1 = destructible, 2 = indestructible
    int activo;         // 1 = visible, 0 = destruido
    struct Bloque* siguiente;
} Bloque;

typedef struct {
    int x, y;
    int tipo;
    int activo;
} BloquePlano;

Bloque* crear_bloque(int x, int y, int tipo);
void destruir_bloques(Bloque* cabeza);
void imprimir_bloques(Bloque* cabeza);

#endif

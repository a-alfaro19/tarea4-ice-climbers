#include <stdlib.h>
#include <stdio.h>
#include "bloque.h"

Bloque* crear_bloque(int x, int y, int tipo) {
    Bloque* b = malloc(sizeof(Bloque));
    b->x = x;
    b->y = y;
    b->tipo = tipo;
    b->activo = 1;
    b->siguiente = NULL;
    return b;
}

void destruir_bloques(Bloque* cabeza) {
    while (cabeza) {
        Bloque* tmp = cabeza;
        cabeza = cabeza->siguiente;
        free(tmp);
    }
}

void imprimir_bloques(Bloque* cabeza) {
    while (cabeza) {
        printf("Bloque en (%d, %d), tipo %d, activo %d\n", cabeza->x, cabeza->y, cabeza->tipo, cabeza->activo);
        cabeza = cabeza->siguiente;
    }
}

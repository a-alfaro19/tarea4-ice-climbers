#include <stdlib.h>
#include <stdio.h>
#include "bloque.h"
/**
 * Crea un nuevo bloque con las coordenadas y tipo especificados.
 *
 * @param x Posición horizontal del bloque.
 * @param y Posición vertical del bloque.
 * @param tipo Tipo de bloque:
 *             0 = vacío,
 *             1 = destructible,
 *             2 = indestructible.
 * @return Puntero al nuevo bloque creado (dinámicamente en memoria).
 */
Bloque* crear_bloque(int x, int y, int tipo) {
    Bloque* b = malloc(sizeof(Bloque));
    b->x = x;
    b->y = y;
    b->tipo = tipo;
    b->activo = 1;
    b->siguiente = NULL;
    return b;
}
/**
 * Libera la memoria de todos los bloques en una lista enlazada.
 *
 * @param cabeza Puntero al primer bloque de la lista.
 */
void destruir_bloques(Bloque* cabeza) {
    while (cabeza) {
        Bloque* tmp = cabeza;
        cabeza = cabeza->siguiente;
        free(tmp);
    }
}
/**
 * Imprime la información de cada bloque en la lista,
 * útil para depuración del mapa.
 *
 * @param cabeza Puntero al primer bloque de la lista.
 */
void imprimir_bloques(Bloque* cabeza) {
    while (cabeza) {
        printf("Bloque en (%d, %d), tipo %d, activo %d\n", cabeza->x, cabeza->y, cabeza->tipo, cabeza->activo);
        cabeza = cabeza->siguiente;
    }
}

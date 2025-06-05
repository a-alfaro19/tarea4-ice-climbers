#ifndef NIVEL_H
#define NIVEL_H

#include "bloque.h"

typedef struct Nivel {
    int indice;              // 0 a 31
    Bloque* bloques;         // lista enlazada de bloques
    struct Nivel* siguiente;
} Nivel;

Nivel* crear_nivel(int indice);
void agregar_bloque_a_nivel(Nivel* nivel, Bloque* b);
void destruir_niveles(Nivel* cabeza);
Nivel* obtener_nivel(Nivel* lista, int indice);
Nivel* obtener_o_crear_nivel(Nivel** mapa_ref, int y);
void vaciar_nivel(int y);

#endif

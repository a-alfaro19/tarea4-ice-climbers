#include <stdlib.h>
#include "nivel.h"

Nivel* crear_nivel(int indice) {
    Nivel* n = malloc(sizeof(Nivel));
    n->indice = indice;
    n->bloques = NULL;
    n->siguiente = NULL;
    return n;
}

void agregar_bloque_a_nivel(Nivel* nivel, Bloque* b) {
    b->siguiente = nivel->bloques;
    nivel->bloques = b;
}

void destruir_niveles(Nivel* cabeza) {
    while (cabeza) {
        Nivel* tmp = cabeza;
        destruir_bloques(cabeza->bloques);
        cabeza = cabeza->siguiente;
        free(tmp);
    }
}

Nivel* obtener_nivel(Nivel* lista, int indice) {
    while (lista) {
        if (lista->indice == indice) return lista;
        lista = lista->siguiente;
    }
    return NULL;
}

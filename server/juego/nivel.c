#include <stdlib.h>
#include "nivel.h"

#include "mapa.h"

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
Nivel* obtener_o_crear_nivel(Nivel** mapa_ref, int y) {
    Nivel* actual = *mapa_ref;
    Nivel* anterior = NULL;

    while (actual) {
        if (actual->indice == y) return actual;
        anterior = actual;
        actual = actual->siguiente;
    }

    // No encontrado, lo creamos
    Nivel* nuevo = crear_nivel(y);
    if (!*mapa_ref) {
        *mapa_ref = nuevo;
    } else {
        anterior->siguiente = nuevo;
    }
    return nuevo;
}

void vaciar_nivel(int y) {
    Nivel* n = obtener_nivel(mapa, y);
    if (!n) return;

    Bloque* b = n->bloques;
    while (b) {
        b->tipo = 0;
        b->activo = 1;  // para que el cliente vea el bloque vacío
        b = b->siguiente;
    }
}
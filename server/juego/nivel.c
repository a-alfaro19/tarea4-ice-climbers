#include <stdlib.h>
#include "nivel.h"
#include "mapa.h"

/**
 * Crea un nuevo nivel sin bloques y con el índice indicado.
 *
 * @param indice Valor vertical (y) del nivel.
 * @return Puntero al nuevo nivel creado.
 */
Nivel* crear_nivel(int indice) {
    Nivel* n = malloc(sizeof(Nivel));
    n->indice = indice;
    n->bloques = NULL;
    n->siguiente = NULL;
    return n;
}
/**
 * Agrega un bloque al inicio de la lista de bloques del nivel.
 *
 * @param nivel Nivel al que se desea agregar el bloque.
 * @param b Puntero al bloque a insertar.
 */
void agregar_bloque_a_nivel(Nivel* nivel, Bloque* b) {
    b->siguiente = nivel->bloques;
    nivel->bloques = b;
}
/**
 * Libera todos los niveles en una lista, así como todos sus bloques internos.
 *
 * @param cabeza Puntero al primer nivel de la lista.
 */
void destruir_niveles(Nivel* cabeza) {
    while (cabeza) {
        Nivel* tmp = cabeza;
        destruir_bloques(cabeza->bloques);
        cabeza = cabeza->siguiente;
        free(tmp);
    }
}
/**
 * Busca un nivel específico en la lista, comparando su índice.
 *
 * @param lista Lista de niveles.
 * @param indice Índice del nivel deseado.
 * @return Puntero al nivel si se encuentra, NULL si no.
 */
Nivel* obtener_nivel(Nivel* lista, int indice) {
    while (lista) {
        if (lista->indice == indice) return lista;
        lista = lista->siguiente;
    }
    return NULL;
}
/**
 * Busca un nivel con el índice dado, y si no existe lo crea y lo inserta al final de la lista.
 *
 * @param mapa_ref Referencia al puntero de la lista de niveles.
 * @param y Índice del nivel a buscar o crear.
 * @return Puntero al nivel resultante.
 */
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
/**
 * Vacía un nivel específico marcando todos sus bloques como vacíos (tipo 0),
 * pero manteniéndolos activos para que el cliente los vea como espacios vacíos.
 *
 * @param y Índice del nivel a vaciar.
 */
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
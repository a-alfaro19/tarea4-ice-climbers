#include <stdlib.h>
#include "mapa.h"

Nivel* mapa = NULL;

#include <stdlib.h>
#include <time.h>
#include "mapa.h"

void inicializar_mapa() {
    mapa = NULL;
    Nivel* ultimo = NULL;

    for (int i = 0; i < 32; i++) {
        int fila_mapa = i * 4; // fila y: cada nivel ocupa 2 líneas verticales
        Nivel* n = crear_nivel(fila_mapa);

        for (int x = 0; x < 30; x++) {
            int tipo = (rand() % 4 == 0) ? 2 : 1;
            if (rand() % 5 != 0) {
                Bloque* b = crear_bloque(x, fila_mapa, tipo);
                agregar_bloque_a_nivel(n, b);
            }
        }

        if (mapa == NULL) {
            mapa = n;
        } else {
            ultimo->siguiente = n;
        }
        ultimo = n;
    }
}


PaqueteBloques obtener_bloques_visibles() {
    PaqueteBloques paquete = {0};
    Nivel* actual = mapa;
    int niveles_visibles = 0;

    while (actual && niveles_visibles < 8) {
        Bloque* b = actual->bloques;
        while (b && paquete.cantidad < MAX_BLOQUES) {
            if (b->activo) {
                paquete.bloques[paquete.cantidad++] = *b;
            }
            b = b->siguiente;
        }
        actual = actual->siguiente;
        niveles_visibles++;
    }

    return paquete;
}

void destruir_mapa() {
    destruir_niveles(mapa);
    mapa = NULL;
}

#include <stdlib.h>
#include "mapa.h"

#include <stdio.h>

Nivel* mapa = NULL;

#include <stdlib.h>
#include <time.h>
#include "mapa.h"

void inicializar_mapa() {
    mapa = NULL;
    Nivel* ultimo = NULL;

    for (int piso = 0; piso < FLOORS_PER_LEVEL; piso++) {
        int y_piso = piso * (TOTAL_FLOOR_HEIGHT + ROWS_BETWEEN_FLOORS);       // y de piso
        int y_prepiso = y_piso - 1;                                           // y del prepiso

        Nivel* nivel_piso = crear_nivel(y_piso);
        Nivel* nivel_prepiso = (y_prepiso >= 0) ? crear_nivel(y_prepiso) : NULL;

        for (int x = 0; x < 30; x++) {
            // Borde fijo
            if ((0 <= x && x <= 3) || (x >= 26)) {
                agregar_bloque_a_nivel(nivel_piso, crear_bloque(x, y_piso, 2));  // fijo
                if (nivel_prepiso) agregar_bloque_a_nivel(nivel_prepiso, crear_bloque(x, y_prepiso, 0));  // vacío
            } else {
                agregar_bloque_a_nivel(nivel_piso, crear_bloque(x, y_piso, 1));  // normal
                if (nivel_prepiso) {
                    int tipo_random = rand() % 3;  // 0 = vacío, 1 = normal, 2 = plano
                    agregar_bloque_a_nivel(nivel_prepiso, crear_bloque(x, y_prepiso, tipo_random));
                }
            }
        }

        if (!mapa) {
            mapa = nivel_prepiso ? nivel_prepiso : nivel_piso;
            ultimo = nivel_piso;
            if (nivel_prepiso) {
                mapa = nivel_prepiso;
                nivel_prepiso->siguiente = nivel_piso;
            }
        } else {
            if (nivel_prepiso) {
                ultimo->siguiente = nivel_prepiso;
                nivel_prepiso->siguiente = nivel_piso;
                ultimo = nivel_piso;
            } else {
                ultimo->siguiente = nivel_piso;
                ultimo = nivel_piso;
            }
        }
    }
}



PaqueteBloques obtener_bloques_visibles() {
    PaqueteBloques paquete = {0};
    Nivel* actual = mapa;
    int niveles_visibles = 0;

    while (actual) {
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

void debug_imprimir_bloques_altos() {
    Nivel* actual = mapa;
    while (actual) {
        Bloque* b = actual->bloques;
        while (b) {
            if (b->y >= 180) {
                printf("Bloque alto: x=%d, y=%d, tipo=%d\n", b->x, b->y, b->tipo);
            }
            b = b->siguiente;
        }
        actual = actual->siguiente;
    }
}

int hay_bloque_en(int x, int y) {
    Nivel* actual = mapa;
    while (actual) {
        Bloque* b = actual->bloques;
        while (b) {
            // Solo verifica bloques activos y sólidos (tipo != 0)
            if (b->activo && b->tipo != 0 && b->x == x && b->y == y) {
                return 1;
            }
            b = b->siguiente;
        }
        actual = actual->siguiente;
    }
    return 0;
}

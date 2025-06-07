#include <stdlib.h>
#include "mapa.h"
#include <stdio.h>
#include <time.h>


Nivel* mapa = NULL; // Variable global del mapa

/**
 * Inicializa el mapa con niveles escalonados y bloques generados aleatoriamente.
 * Alterna entre niveles normales y niveles de bonus, llenando con bloques indestructibles o destructibles.
 */
void inicializar_mapa() {
    mapa = NULL;

    for (int piso = 0; piso < FLOORS_PER_LEVEL; piso++) {
        int y_piso = piso * (TOTAL_FLOOR_HEIGHT + ROWS_BETWEEN_FLOORS);
        int y_prepiso = y_piso - 1;
        int y_postpiso = y_piso + 1;

        Nivel* nivel_piso = obtener_o_crear_nivel(&mapa, y_piso);
        Nivel* nivel_prepiso = (y_prepiso >= 0) ? obtener_o_crear_nivel(&mapa, y_prepiso) : NULL;
        Nivel* nivel_postpiso = (y_postpiso < TOTAL_ROWS) ? obtener_o_crear_nivel(&mapa, y_postpiso) : NULL;

        for (int x = 0; x < 30; x++) {
            // Niveles normales (0 a 8)
            if (piso < 9) {
                if (x <= 3 || x >= 26) {
                    agregar_bloque_a_nivel(nivel_piso, crear_bloque(x, y_piso, 2)); // indestructible
                    if (nivel_prepiso) {
                        agregar_bloque_a_nivel(nivel_prepiso, crear_bloque(x, y_prepiso, 0)); // vacío
                    }
                } else {
                    agregar_bloque_a_nivel(nivel_piso, crear_bloque(x, y_piso, 1)); // destructible
                    if (nivel_prepiso) {
                        int tipo_random = rand() % 3;
                        agregar_bloque_a_nivel(nivel_prepiso, crear_bloque(x, y_prepiso, tipo_random));
                    }
                }
            }
            // Fase bonus (9 a 15)
            else {
                int borde = (x <= 2 || x >= 27);
                int prob = rand() % 100;

                // Bloques indestructibles o huecos
                if (borde || prob < 80) {
                    agregar_bloque_a_nivel(nivel_piso, crear_bloque(x, y_piso, 2));  // indestructible
                }
                // 20% hueco (no se agrega nada)

                // Bloques extra en el prepiso (escalones)
                if (nivel_prepiso && rand() % 100 < 30) {
                    agregar_bloque_a_nivel(nivel_prepiso, crear_bloque(x, y_prepiso, 2));
                }
            }
        }
    }
}

/**
 * Recolecta todos los bloques activos del mapa en un solo paquete,
 * para ser enviado al cliente.
 */
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
/**
 * Libera la memoria del mapa y todos los bloques contenidos en él.
 */
void destruir_mapa() {
    destruir_niveles(mapa);
    mapa = NULL;
}
/**
 * Imprime todos los bloques ubicados en las filas más altas del mapa (y ≥ 180).
 * Útil para depuración visual del mapa.
 */
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
/**
 * Verifica si hay un bloque sólido (tipo ≠ 0) y activo en una posición dada.
 *
 * @param x Columna del mapa.
 * @param y Fila del mapa.
 * @return 1 si existe un bloque sólido activo, 0 en caso contrario.
 */
int hay_bloque_en(int x, int y) {
    Nivel* actual = mapa;
    while (actual) {
        Bloque* b = actual->bloques;
        while (b) {
            if (b->x == x && b->y == y && b->activo && b->tipo != 0) {
                return 1; // Detecta un bloque sólido activo
            }
            b = b->siguiente;
        }
        actual = actual->siguiente;
    }
    return 0;
}

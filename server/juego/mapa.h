#ifndef MAPA_H
#define MAPA_H

#include "nivel.h"

#define MAX_BLOQUES 3000  // máximo a enviar
#define FLOORS_PER_LEVEL 16
#define FLOOR_HEIGHT 1
#define PRE_FLOOR_HEIGHT 1
#define ROWS_BETWEEN_FLOORS 4
#define TOTAL_FLOOR_HEIGHT (FLOOR_HEIGHT + PRE_FLOOR_HEIGHT)
#define TOTAL_ROWS_FOR_FLOORS  ((FLOORS_PER_LEVEL - 1) * TOTAL_FLOOR_HEIGHT)
#define TOTAL_ROWS_BETWEEN_FLOORS ((FLOORS_PER_LEVEL - 1) * ROWS_BETWEEN_FLOORS)
#define TOTAL_ROWS (1 + TOTAL_ROWS_FOR_FLOORS + TOTAL_ROWS_BETWEEN_FLOORS)

typedef struct {
    int cantidad;
    Bloque bloques[MAX_BLOQUES];
} PaqueteBloques;

extern Nivel* mapa;

void inicializar_mapa();
PaqueteBloques obtener_bloques_visibles();  // Solo primeros 4 niveles
void destruir_mapa();
void debug_imprimir_bloques_altos();
// Revisa si hay un bloque activo (no vacío) en una posición
int hay_bloque_en(int x, int y);
#endif

#ifndef MAPA_H
#define MAPA_H

#include "nivel.h"

#define MAX_BLOQUES 512  // máximo a enviar

typedef struct {
    int cantidad;
    Bloque bloques[MAX_BLOQUES];
} PaqueteBloques;

extern Nivel* mapa;

void inicializar_mapa();
PaqueteBloques obtener_bloques_visibles();  // Solo primeros 4 niveles
void destruir_mapa();

#endif

#ifndef MAPA_H
#define MAPA_H

#include "nivel.h"
#include "constantes.h"
/**
 * Estructura para enviar bloques planos al cliente.
 */
typedef struct {
    int cantidad;
    Bloque bloques[MAX_BLOQUES];
} PaqueteBloques;
/**
 * Variable global que representa el mapa del juego (lista enlazada de niveles).
 */
extern Nivel* mapa;
/**
 * Inicializa la estructura del mapa con bloques distribuidos por nivel.
 */
void inicializar_mapa();
/**
 * Retorna los bloques visibles actuales del mapa en forma de paquete plano.
 */
PaqueteBloques obtener_bloques_visibles();  // Solo primeros 4 niveles
/**
 * Libera la memoria del mapa y lo reinicia.
 */
void destruir_mapa();
/**
 * Imprime en consola todos los bloques ubicados en la parte superior del mapa.
 * Útil para depuración.
 */
void debug_imprimir_bloques_altos();

/**
 * Verifica si hay un bloque sólido y activo en una posición específica.
 *
 * @param x Columna del mapa.
 * @param y Fila del mapa.
 * @return 1 si hay bloque, 0 si no.
 */
int hay_bloque_en(int x, int y);
#endif

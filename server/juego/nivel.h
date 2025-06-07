#ifndef NIVEL_H
#define NIVEL_H

#include "bloque.h"
/**
 * Representa un nivel (una fila específica del mapa vertical).
 * Contiene una lista de bloques y un puntero al siguiente nivel.
 */
typedef struct Nivel {
    int indice;              // Número que representa la fila vertical (y)
    Bloque* bloques;         // Lista enlazada de bloques en esta fila
    struct Nivel* siguiente; // Siguiente nivel en la lista (orden vertical)
} Nivel;
/**
 * Crea un nuevo nivel vacío con el índice especificado.
 *
 * @param indice Valor de y que representa la altura del nivel.
 * @return Puntero al nuevo nivel.
 */
Nivel* crear_nivel(int indice);
/**
 * Agrega un bloque a la lista de bloques del nivel especificado.
 * El bloque se inserta al inicio de la lista.
 *
 * @param nivel Nivel al que se quiere agregar el bloque.
 * @param b Puntero al bloque que se va a insertar.
 */
void agregar_bloque_a_nivel(Nivel* nivel, Bloque* b);

/**
 * Libera la memoria de todos los niveles y sus bloques.
 *
 * @param cabeza Puntero al primer nivel de la lista.
 */
void destruir_niveles(Nivel* cabeza);

/**
 * Busca un nivel específico por su índice (y).
 *
 * @param lista Lista enlazada de niveles.
 * @param indice Índice del nivel que se desea encontrar.
 * @return Puntero al nivel si existe, NULL si no.
 */
Nivel* obtener_nivel(Nivel* lista, int indice);

/**
 * Busca un nivel existente o lo crea si no existe.
 *
 * @param mapa_ref Referencia al puntero de la lista de niveles.
 * @param y Índice del nivel buscado o a crear.
 * @return Puntero al nivel correspondiente.
 */
Nivel* obtener_o_crear_nivel(Nivel** mapa_ref, int y);

/**
 * Marca todos los bloques de un nivel como vacíos (tipo 0) pero visibles,
 * útil para vaciar niveles inferiores al avanzar.
 *
 * @param y Índice vertical del nivel que se quiere vaciar.
 */
void vaciar_nivel(int y);

#endif

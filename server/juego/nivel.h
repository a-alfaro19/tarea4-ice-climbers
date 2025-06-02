#ifndef NIVEL_H
#define NIVEL_H

#include "bloque.h"

typedef struct Nivel {
    int indice;              // Representa el número del nivel en el mapa (0 a 31)
    Bloque* bloques;         // Lista enlazada de los bloques del nivel
    struct Nivel* siguiente; // Referencia al siguiente Nivel
} Nivel;

/**
* @brief Crea una estructura Nivel
* @param indice Índice del nivel a crear
* @return Puntero al Nivel creado
*/
Nivel* crear_nivel(int indice);

/**
* @brief Agrega un bloque a un nivel dado
* @param nivel Nivel donde agregar el bloque
* @param b Bloque a agregar al nivel
*/
void agregar_bloque_a_nivel(Nivel* nivel, Bloque* b);


void destruir_niveles(Nivel* cabeza);

/**
* @brief Obtiene un Nivel especifico del mapa
* @param indice Índice del nivel a obtener
* @return El Nivel del mapa en la posición indicada
*/
Nivel* obtener_nivel(Nivel* lista, int indice);

#endif

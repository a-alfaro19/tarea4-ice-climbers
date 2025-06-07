#ifndef FRUTA_H
#define FRUTA_H
#include "constantes.h"
/**
 * Tipos de frutas/verduras disponibles en la fase de bonus.
 * Cada tipo tiene un valor en puntos:
 * - NARANJA = 100 pts
 * - BANANO = 200 pts
 * - BERENJENA = 300 pts
 * - LECHUGA = 400 pts
 */
typedef enum {
    NARANJA = 0,
    BANANO,
    BERENJENA,
    LECHUGA
} TipoFruta;

/**
 * Representa una fruta o verdura en el mapa.
 */
typedef struct {
    int x, y; // Coordenadas de la fruta en el mapa
    TipoFruta tipo;   // Tipo de fruta
    int activa; // 1 = visible y se puede recoger, 0 = ya fue tomada o eliminada
} Fruta;
/**
 * Contenedor de frutas, máximo 4 por nivel de bonus.
 */
typedef struct {
    Fruta frutas[MAX_FRUTAS]; // Arreglo con las frutas activas
    int cantidad; // Número de frutas activas actualmente
} ListaFrutas;

#endif

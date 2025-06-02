#ifndef JUGADOR_H
#define JUGADOR_H

typedef struct Jugador {
    char nombre[10];
    int x, y;           // posición actual
    int vidas;          // vidas restantes
    int puntaje;        // puntos acumulados
    char direccion;     // dirección actual ('L' o 'R')
} Jugador;

void perder_vida(Jugador* j);
void sumar_puntaje(Jugador* j, int valor);

#endif

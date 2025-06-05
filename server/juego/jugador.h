#ifndef JUGADOR_H
#define JUGADOR_H

typedef struct Jugador {
    char nombre[10];
    float y_real;
    int x, y;            // posición en el mapa
    int vidas;
    int puntaje;
    char direccion;
    float vy;            // velocidad vertical (salto)
    int en_el_aire;      // 1 = sí, 0 = no
} Jugador;

void perder_vida(Jugador* j);
void sumar_puntaje(Jugador* j, int valor);

#endif



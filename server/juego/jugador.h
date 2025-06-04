#ifndef JUGADOR_H
#define JUGADOR_H

typedef struct Jugador {
    char nombre[10];
    int x, y;
    int vidas;
    int puntaje;
    char direccion;
    int saltando;  // nuevo campo: 1 si acaba de brincar
} Jugador;


void perder_vida(Jugador* j);
void sumar_puntaje(Jugador* j, int valor);

#endif

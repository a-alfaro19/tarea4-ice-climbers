#ifndef TIPOS_H
#define TIPOS_H

// Jugador (Popo o Nana)
typedef struct {
    char nombre[10];   // "Popo" o "Nana"
    int x, y;          // Posición en el mapa
    int vidas;
    int puntaje;
    char direccion;    // 'L' o 'R'
} Jugador;

// Obstáculo: Yeti, Ave, Hielo
typedef struct {
    char tipo[10];     // "yeti", "ave", "hielo"
    int x, y;
    char direccion;    // 'L', 'R', 'U', 'D'
    int activo;        // 1 = activo, 0 = destruido
} Obstaculo_st;

// Fruta para fase bonus
typedef struct {
    char tipo[15];     // "naranja", "banana", etc.
    int valor;         // 100, 200, 300, 400
    int x, y;
    int recolectada;   // 1 = sí, 0 = no
} Fruta;

// Nivel individual
typedef struct {
    int id;
    Obstaculo_st obstaculos[10];
    Fruta frutas[4];
    int num_obstaculos;
    int num_frutas;
} Nivel;

// Juego completo
typedef struct {
    Jugador jugadores[2];     // Popo y Nana
    Nivel niveles[10];        // Hasta 10 niveles
    int nivel_actual;
    int en_fase_bonus;
    int velocidad;
} Juego;

#endif

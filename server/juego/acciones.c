#include "acciones.h"
#include "mapa.h"
#include <stdio.h>

// Aplica gravedad hasta encontrar un piso debajo
void aplicar_gravedad(Jugador* j) {
    while (!hay_bloque_en(j->x, j->y - 1)) {
        j->y--;
    }
}

// Movimiento horizontal si no hay bloque sólido enfrente
void mover_jugador(Jugador* j, char dir) {
    int nuevo_x = j->x + (dir == 'L' ? -1 : dir == 'R' ? 1 : 0);
    if (nuevo_x < 0 || nuevo_x >= 30) return;

    if (!hay_bloque_en(nuevo_x, j->y)) {
        j->x = nuevo_x;
        j->direccion = dir;
        printf("%s se movió a x=%d\n", j->nombre, j->x);
    } else {
        printf("%s no pudo moverse, bloque en x=%d y=%d\n", j->nombre, nuevo_x, j->y);
    }

    aplicar_gravedad(j);
}

void brincar_jugador(Jugador* j, Nivel* mapa) {
    if (j->saltando == 0 && !hay_bloque_en(j->x, j->y+1)) {
        j->saltando = 5;  // por ejemplo, puede subir 5 frames seguidos
        printf("%s comenzó a saltar\n", j->nombre);
    } else {
        printf("%s no puede saltar ahora\n", j->nombre);
    }
}



// Golpear bloque destructible directamente arriba
void golpear(Jugador* j, Nivel* nivel) {
    Bloque* b = nivel->bloques;
    while (b) {
        if (b->x == j->x && b->y == j->y + 1 && b->activo && b->tipo == 1) {
            b->activo = 0;
            printf("%s destruyó bloque en (%d, %d)\n", j->nombre, b->x, b->y);
            return;
        }
        b = b->siguiente;
    }
    printf("%s golpeó pero no había bloque destructible arriba\n", j->nombre);
}

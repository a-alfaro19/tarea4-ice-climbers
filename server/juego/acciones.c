
#include "acciones.h"
#include <stdio.h>

void mover_jugador(Jugador* j, char dir) {
    if (dir == 'L') { // Move left
        if (j->x-1 < 0) {
            j->x = 20;
        } else {
            j->x--;
        }
        j->direccion = 'L';
    } else if (dir == 'R') { // Move Right
        if (j->x+1 > 20) {
            j->x = 0;
        } else {
            j->x++;
        }
        j->direccion = 'R';
    }
    printf("%s se movi\xC3\xB3 a %d\n", j->nombre, j->x);
}

void brincar_jugador(Jugador* j) {
    j->y++;
    printf("%s brinc\xC3\xB3 a %d\n", j->nombre, j->y);
}

void golpear(Jugador* j, Nivel* nivel) {
    Bloque* b = nivel->bloques;
    while (b) {
        if (b->x == j->x && b->y == j->y - 1 && b->activo && b->tipo == 1) {
            b->activo = 0;
            printf("%s destruy\xC3\xB3 bloque en (%d, %d)\n", j->nombre, b->x, b->y);
            break;
        }
        b = b->siguiente;
    }
}

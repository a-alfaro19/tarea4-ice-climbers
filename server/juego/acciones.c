#include "acciones.h"
#include "mapa.h"
#include <stdio.h>
#include <math.h>
#define GRAVEDAD -0.25f
#define FUERZA_SALTO 3.5f
#define MAX_ALTURA_SALTO 5.0f   // Altura máxima en bloques

void actualizar_fisica(Jugador* j) {
    if (!j->en_el_aire) {
        if (!hay_bloque_en(j->x, j->y - 1)) {
            j->vy = -0.1f;  // inicia caída leve
            j->en_el_aire = 1;
        } else {
            return;
        }
    }

    // Guardar dirección y destino
    int direccion = (j->vy > 0) ? 1 : -1;
    int destino_y = j->y + direccion;

    // Limitar vy
    j->vy += GRAVEDAD;
    if (j->vy < -3.0f) j->vy = -3.0f;

    // Evita salirse del mapa
    if (destino_y < 0) {
        j->y = 0;
        j->vy = 0;
        j->en_el_aire = 0;
        return;
    }

    if (destino_y >= TOTAL_ROWS) {
        j->y = TOTAL_ROWS - 1;
        j->vy = 0;
        j->en_el_aire = 0;
        return;
    }

    if (hay_bloque_en(j->x, destino_y)) {
        // Colisión con piso (al caer)
        if (direccion < 0) {
            j->en_el_aire = 0;
        }
        j->vy = 0;
    } else {
        // Movimiento vertical
        j->y = destino_y;
    }
}


// Movimiento lateral
void mover_jugador(Jugador* j, char dir) {
    int dx = (dir == 'L') ? -1 : 1;
    int nuevo_x = j->x + dx;

    if (nuevo_x >= 0 && nuevo_x < 30 && !hay_bloque_en(nuevo_x, j->y)) {
        j->x = nuevo_x;
        j->direccion = dir;
        printf("%s se movió a x=%d\n", j->nombre, j->x);
    }
}

// Inicia salto si está en el suelo
void brincar_jugador(Jugador* j, Nivel* mapa) {
    if (!j->en_el_aire && !hay_bloque_en(j->x, j->y + 2)) {
        j->vy = FUERZA_SALTO;
        j->en_el_aire = 1;
        printf("%s inició salto con vy=%.2f\n", j->nombre, j->vy);
    } else {
        printf("%s no puede saltar ahora\n", j->nombre);
    }
}


// Golpear bloque destructible arriba
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
    printf("%s golpeó pero no había bloque destructible\n", j->nombre);
}


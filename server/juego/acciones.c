#include "acciones.h"
#include "mapa.h"
#include <stdio.h>
#include <math.h>

#include "acciones.h"
#include "mapa.h"
#include <stdio.h>
#include <math.h>
#define GRAVEDAD -0.08f           // caída más suave
#define FUERZA_SALTO 5.5f         // salto más alto
#define VELOCIDAD_TERMINAL -1.5f // caída controlada
#define PASO_VERTICAL 0.2f        // precisión de movimiento vertical

void actualizar_fisica(Jugador* j) {
    if (!j->en_el_aire && !hay_bloque_en(j->x, j->y - 1)) {
        j->en_el_aire = 1;
        j->vy = -0.1f;
    }

    if (!j->en_el_aire) return;

    j->vy += GRAVEDAD;
    if (j->vy < VELOCIDAD_TERMINAL) j->vy = VELOCIDAD_TERMINAL;

    float y = (float)j->y;
    float y_objetivo = y + j->vy;
    float paso = (j->vy > 0) ? PASO_VERTICAL : -PASO_VERTICAL;

    while ((paso > 0 && y < y_objetivo) || (paso < 0 && y > y_objetivo)) {
        y += paso;
        int y_int = (int)(y + 0.5f);

        // Salirse del mapa
        if (y_int < 0 || y_int >= TOTAL_ROWS) {
            j->y = (y_int < 0) ? 0 : TOTAL_ROWS - 1;
            j->vy = 0;
            j->en_el_aire = 0;
            return;
        }

        if (hay_bloque_en(j->x, y_int)) {
            if (j->vy < 0) {
                j->y = y_int + 1;
                j->en_el_aire = 0;  // Aterriza solo si choca con el suelo
            } else {
                j->y = y_int - 1;
                // sigue en el aire aunque pegue techo
            }
            j->vy = 0;
            return;
        }


        j->y = y_int;
    }
}

void brincar_jugador(Jugador* j) {
    printf("Intentando brincar... en_el_aire=%d, bloque_abajo=%d\n",
           j->en_el_aire, hay_bloque_en(j->x, j->y - 1));

    if (!j->en_el_aire && hay_bloque_en(j->x, j->y - 1)) {
        j->vy = 5.5f;
        j->en_el_aire = 1;
        printf("%s inició salto con vy=%.2f\n", j->nombre, j->vy);
    } else {
        printf("%s no puede saltar ahora\n", j->nombre);
    }
}



void mover_jugador(Jugador* j, char dir) {
    int dx = (dir == 'L') ? -1 : 1;
    int nuevo_x = j->x + dx;

    if (nuevo_x >= 0 && nuevo_x < 30 && !hay_bloque_en(nuevo_x, j->y)) {
        j->x = nuevo_x;
        j->direccion = dir;
        printf("%s se movió a x=%d\n", j->nombre, j->x);
    }
}



void golpear(Jugador* j, Nivel* _) {
    Nivel* actual = mapa;
    while (actual) {
        Bloque* b = actual->bloques;
        while (b) {
            if (b->x == j->x && b->y == j->y + 1 && b->activo && b->tipo == 1) {
                b->activo = 0;
                printf("%s destruyó bloque en (%d, %d)\n", j->nombre, b->x, b->y);
                return;
            }
            b = b->siguiente;
        }
        actual = actual->siguiente;
    }
    printf("%s golpeó pero no había bloque destructible\n", j->nombre);
}

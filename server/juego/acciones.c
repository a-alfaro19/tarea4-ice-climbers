#include "acciones.h"
#include "mapa.h"
#include <stdio.h>
#include <math.h>

#include "acciones.h"
#include "mapa.h"
#include <stdio.h>
#include <math.h>
#define GRAVEDAD -0.05f           // caída más suave
#define FUERZA_SALTO 5.5f         // salto más alto
#define VELOCIDAD_TERMINAL -2.5f // caída controlada
#define PASO_VERTICAL 0.01f        // precisión de movimiento vertical

void actualizar_fisica(Jugador* j) {
    //  Punto 1: Verificación de si debería empezar a caer
    int y_abajo = j->y - 1;
    int bloque_abajo = hay_bloque_en(j->x, y_abajo);
    //printf("[CHECK CAIDA] y_real=%.2f  y=%d  y_abajo=%d  bloque_abajo=%d  en_el_aire=%d\n",
           //j->y_real, j->y, y_abajo, bloque_abajo, j->en_el_aire);

    if (!j->en_el_aire && !bloque_abajo) {
        j->en_el_aire = 1;
        j->vy = -0.1f;
        printf("→ Comienza a caer\n");
    }

    if (!j->en_el_aire) return;

    //  Aplicar gravedad
    j->vy += GRAVEDAD;
    if (j->vy < VELOCIDAD_TERMINAL) j->vy = VELOCIDAD_TERMINAL;

    float y_objetivo = j->y_real + j->vy;
    float paso = (j->vy > 0) ? PASO_VERTICAL : -PASO_VERTICAL;

    while ((paso > 0 && j->y_real < y_objetivo) || (paso < 0 && j->y_real > y_objetivo)) {
        j->y_real += paso;
        int y_int = (int)(j->y_real + 0.5f);

        if (y_int < 0 || y_int >= TOTAL_ROWS) {
            j->y_real = (y_int < 0) ? 0 : TOTAL_ROWS - 1;
            j->y = (int)(j->y_real + 0.5f);
            j->vy = 0;
            j->en_el_aire = 0;
            printf("Fuera del mapa, ajustado a y=%d\n", j->y);
            return;
        }

        if (hay_bloque_en(j->x, y_int)) {
            int bloque_arriba = hay_bloque_en(j->x, y_int + 1);
            printf("[COLISION] en y_int=%d, vy=%.2f, bloque_arriba=%d\n", y_int, j->vy, bloque_arriba);

            if (j->vy < 0) {
                if (!bloque_arriba) {
                    j->y_real = (float)(y_int + 1);
                    j->vy = 0;
                    j->en_el_aire = 0;
                    j->y = y_int + 1;
                    printf("Aterrizó en y=%d (y_real=%.2f)\n", j->y, j->y_real);
                } else {
                    printf("No pudo aterrizar, hay bloque arriba en y=%d\n", y_int + 1);
                }
            } else {
                j->y_real = (float)(y_int - 1);
                j->vy = 0;
                printf("Pegó techo en y=%d\n", y_int);
            }
            break;
        }
    }

    j->y = (int)(j->y_real + 0.5f);
    printf("[ACTUALIZADO] y_real=%.2f  y=%d  en_el_aire=%d  vy=%.2f\n\n",
           j->y_real, j->y, j->en_el_aire, j->vy);
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

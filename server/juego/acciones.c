#include "acciones.h"
#include "../juego/nivel.h"
#include "../juego/bloque.h"
#include "jugador.h"
#include "constantes.h" // Constantes físicas del juego
#include "acciones.h"
#include "mapa.h"
#include <stdio.h>
#include <math.h>


/**
 * Aplica gravedad y movimiento vertical al jugador,
 * verificando colisiones con bloques y suelo.
 */
void actualizar_fisica(Jugador* j) {
    // Verifica si debería empezar a caer (no hay bloque debajo y no está en el aire)
    int y_abajo = j->y - 1;
    int bloque_abajo = hay_bloque_en(j->x, y_abajo);

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

    // Movimiento vertical progresivo, paso a paso
    while ((paso > 0 && j->y_real < y_objetivo) || (paso < 0 && j->y_real > y_objetivo)) {
        j->y_real += paso;
        int y_int = (int)(j->y_real + 0.5f);
        // Límite del mapa
        if (y_int < 0 || y_int >= TOTAL_ROWS) {
            j->y_real = (y_int < 0) ? 0 : TOTAL_ROWS - 1;
            j->y = (int)(j->y_real + 0.5f);
            j->vy = 0;
            j->en_el_aire = 0;
            return;
        }
        // Colisión con un bloque
        if (hay_bloque_en(j->x, y_int)) {
            int bloque_arriba = hay_bloque_en(j->x, y_int + 1);

            if (j->vy < 0) {// Cayendo
                if (!bloque_arriba) {
                    // Aterriza
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
    // Actualizar valor entero de y para la lógica del juego
    j->y = (int)(j->y_real + 0.5f);
}


/**
 * Permite al jugador iniciar un salto si está en el suelo.
 */
void brincar_jugador(Jugador* j) {
    printf("Intentando brincar... en_el_aire=%d, bloque_abajo=%d\n",
           j->en_el_aire, hay_bloque_en(j->x, j->y - 1));

    if (!j->en_el_aire && hay_bloque_en(j->x, j->y - 1)) {
        j->vy = FUERZA_SALTO; // Velocidad inicial del salto
        j->en_el_aire = 1;
        printf("%s inició salto con vy=%.2f\n", j->nombre, j->vy);
    } else {
        printf("%s no puede saltar ahora\n", j->nombre);
    }
}

/**
 * Mueve lateralmente al jugador si el camino está libre.
 * 'dir' debe ser 'L' o 'R' (izquierda o derecha).
 */
void mover_jugador(Jugador* j, char dir) {
    int dx = (dir == 'L') ? -1 : 1;
    int nuevo_x = j->x + dx;
    // Validar límites y colisión
    if (nuevo_x >= 0 && nuevo_x < 30 && !hay_bloque_en(nuevo_x, j->y)) {
        j->x = nuevo_x;
        j->direccion = dir;
        printf("%s se movió a x=%d\n", j->nombre, j->x);
    }
}


/**
 * Permite destruir un bloque destructible justo encima del jugador.
 */
void golpear(Jugador* j, Nivel* _) {
    Nivel* actual = mapa;
    while (actual) {
        Bloque* b = actual->bloques;
        while (b) {
            // Busca bloque destructible justo arriba
            if (b->x == j->x && b->y == j->y + 1 && b->activo && b->tipo == 1) {
                b->activo = 0; // Destruir bloque
                printf("%s destruyó bloque en (%d, %d)\n", j->nombre, b->x, b->y);
                return;
            }
            b = b->siguiente;
        }
        actual = actual->siguiente;
    }
    printf("%s golpeó pero no había bloque destructible\n", j->nombre);
}

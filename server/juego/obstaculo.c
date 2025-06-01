#include "obstaculo.h"
#include <stdlib.h>
#include "../util/log.h"

static void mover_yeti(Obstaculo *obs) {
    // Solo se mueve hacia la izquierda o a la derecha
    switch (obs->direccion) {
        case DER:
            obs->pos.x += 1;
            break;

        case IZQ:
            obs->pos.x -= 1;
            break;

        default:
            // Mostrar mensaje de error
            log_error("Dirección inválida para Yeti. Solo puede moverse IZQ o DER");
            exit(1);
    }
}

static void mover_ave(Obstaculo *obs) {
    // Se mueve en diagonal
    switch (obs->direccion) {
        // Diagonal Derecha Arriba
        case DER:
            obs->pos.x += 1;
            obs->pos.y -= 1;
            break;

        // Diagonal Izquierda Abajo
        case IZQ:
            obs->pos.x -= 1;
            obs->pos.y += 1;

        default:
            // Mostrar mensaje de error
            log_error("Dirección inválida para Ave. Solo puede moverse IZQ o DER");
            exit(1);
    }
}

static void mover_bloque(Obstaculo *obs) {
    // Solo se mueve hacia abajo
    obs->pos.y += 1;
}

static void dibujar_yeti(Obstaculo* obs) {}

static void dibujar_ave(Obstaculo* obs) {}

static void dibujar_bloque(Obstaculo* obs) {}

Obstaculo* crear_obstaculo(const TipoObstaculo tipo, const int x, const int y, const int ancho, const int alto) {
    Obstaculo* nuevo_obstaculo = malloc(sizeof(Obstaculo));
    if (nuevo_obstaculo == NULL) return NULL;

    // Definir origen
    nuevo_obstaculo->pos.x = x;
    nuevo_obstaculo->pos.y = y;

    nuevo_obstaculo->dimensiones.ancho = ancho;
    nuevo_obstaculo->dimensiones.alto = alto;

    nuevo_obstaculo->activo = 1;
    nuevo_obstaculo->tipo = tipo;


    switch (tipo) {
        case YETI:
            nuevo_obstaculo->mover = mover_yeti;
            nuevo_obstaculo->dibujar = dibujar_yeti;
            break;
        case AVE:
            nuevo_obstaculo->direccion = DER;
            nuevo_obstaculo->mover = mover_ave;
            nuevo_obstaculo->dibujar = dibujar_ave;
            break;
        case BLOQUE_HIELO:
            nuevo_obstaculo->direccion = ABAJO;
            nuevo_obstaculo->mover = mover_bloque;
            nuevo_obstaculo->dibujar = dibujar_bloque;
            break;

        default:
            free(nuevo_obstaculo);
            return NULL;
    }

    return nuevo_obstaculo;
}

int destruir_obstaculo(const Obstaculo* obs) {
    if (obs == NULL) return 0; // El obstáculo no existe
    free((void*)obs); // Liberar memoria
    return 1;
}
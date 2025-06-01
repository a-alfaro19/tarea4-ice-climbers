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

static void dibujar_obstaculo(const Obstaculo* obs) {
    switch (obs->tipo) {
        case YETI:
            // Cargar el dibujo del yeti
            break;

        case AVE:
            // Cargar el dibujo del ave
            break;

        case BLOQUE_HIELO:
            // Cargar el dibujo del bloque de hielo
            break;

        default:
            // Mostrar mensaje de error
            log_error("Tipo de obstáculo inválido para dibujar");
            exit(1);
    }
}

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

    nuevo_obstaculo->dibujar = dibujar_obstaculo;


    switch (tipo) {
        case YETI:
            nuevo_obstaculo->mover = mover_yeti;
            break;
        case AVE:
            nuevo_obstaculo->direccion = DER;
            nuevo_obstaculo->mover = mover_ave;
            break;
        case BLOQUE_HIELO:
            nuevo_obstaculo->direccion = ABAJO;
            nuevo_obstaculo->mover = mover_bloque;
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

int crear_y_agregar_obstaculo(Nivel *nivel, TipoObstaculo tipo, int x, int y) {
    if (nivel->num_obstaculos >= MAX_OBSTACULOS) {
        log_error("Límite de obstáculos alcanzado en el nivel.");
        return 0;
    }

    Obstaculo *nuevo = crear_obstaculo(tipo, x, y, 1, 1); // tamaño 1x1 por defecto
    if (!nuevo) {
        log_error("Error al crear obstáculo.");
        return 0;
    }

    agregar_obstaculo_a_nivel(nivel, nuevo);
    log_info("Obstáculo creado y agregado correctamente.");
    return 1;
}
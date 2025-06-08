#include <stdio.h>
#include <winsock2.h>
#include "../juego/mapa.h"
#include "../juego/juego.h"
#include "mensajes.h"
#include <string.h>

/**
 * Envía al cliente toda la información del estado actual del juego:
 * - Datos de los 2 jugadores (nombre, posición, puntaje, etc.)
 * - Obstáculos activos
 * - Frutas activas
 * - Estado general del juego (nivel, fase bonus, velocidad)
 * - Estado del pterodáctilo
 */
int enviar_juego(SOCKET socket, Juego* juego) {
    for (int i = 0; i < 2; i++) {
        Jugador* j = &juego->jugadores[i];

        if (send(socket, j->nombre, 10, 0) != 10) return -1;
        if (send(socket, &j->x, 4, 0) != 4) return -1;
        if (send(socket, &j->y, 4, 0) != 4) return -1;
        if (send(socket, &j->vidas, 4, 0) != 4) return -1;
        if (send(socket, &j->puntaje, 4, 0) != 4) return -1;
        if (send(socket, &j->vy, sizeof(float), 0) != sizeof(float)) return -1;
        if (send(socket, &j->en_el_aire, sizeof(int), 0) != sizeof(int)) return -1;

        if (send(socket, &j->direccion, 1, 0) != 1) return -1;
        char relleno = 0;
        if (send(socket, &relleno, 1, 0) != 1) return -1;

        if (send(socket, &j->puntos_hielo, 4, 0) != 4) return -1;
        if (send(socket, &j->puntos_ave, 4, 0) != 4) return -1;
        if (send(socket, &j->puntos_yeti, 4, 0) != 4) return -1;
        if (send(socket, &j->puntos_naranja, 4, 0) != 4) return -1;
        if (send(socket, &j->puntos_banano, 4, 0) != 4) return -1;
        if (send(socket, &j->puntos_berenjena, 4, 0) != 4) return -1;
        if (send(socket, &j->puntos_lechuga, 4, 0) != 4) return -1;

    }

    // Enviar cantidad de obstáculos
    const Obstacle* obstacles = juego->obstacles.obstacles;
    const int size = juego->obstacles.size;

    if (send(socket, &size, 4, 0) != 4) return -1;

    // Enviar cada obstáculo
    for (int i = 0; i < size; i++) {
        Obstacle* obstacle = &obstacles[i];

        int typeInt = obstacle->type;
        if (send(socket, &typeInt, 4, 0) != 4) return -1;
        if (send(socket, &obstacle->x, 4, 0) != 4) return -1;
        if (send(socket, &obstacle->y, 4, 0) != 4) return -1;
    }
    // Enviar frutas activas
    int cantidad = juego->frutas.cantidad;
    send(socket, &cantidad, 4, 0);
    for (int i = 0; i < cantidad; i++) {
        Fruta* f = &juego->frutas.frutas[i];
        send(socket, &f->x, 4, 0);
        send(socket, &f->y, 4, 0);
        send(socket, &f->tipo, 4, 0);
        send(socket, &f->activa, 4, 0);
    }


    // Enviar variables del juego
    if (send(socket, &juego->nivel_actual, 4, 0) != 4) return -1;
    if (send(socket, &juego->en_fase_bonus, 4, 0) != 4) return -1;
    if (send(socket, &juego->velocidad, 4, 0) != 4) return -1;

    // Enviar pterodáctilo
    if (send(socket, &juego->ptero.activo, 4, 0) != 4) return -1;
    if (send(socket, &juego->ptero.x, 4, 0) != 4) return -1;
    if (send(socket, &juego->ptero.y, 4, 0) != 4) return -1;
    if (send(socket, &juego->ptero.direccion, 4, 0) != 4) return -1;

    return 0;
}
/**
 * Envía al cliente los bloques activos visibles del mapa.
 * Primero se envía la cantidad, luego los bloques en formato plano.
 */
int enviar_bloques(SOCKET socket, const PaqueteBloques* paquete) {
    int cantidad = paquete->cantidad;
    if (send(socket, (const char*)&cantidad, sizeof(int), 0) != sizeof(int)) {
        printf("Error al enviar la cantidad de bloques\n");
        return -1;
    }

    BloquePlano planos[MAX_BLOQUES];
    for (int i = 0; i < cantidad; i++) {
        planos[i].x = paquete->bloques[i].x;
        planos[i].y = paquete->bloques[i].y;
        planos[i].tipo = paquete->bloques[i].tipo;
        planos[i].activo = paquete->bloques[i].activo;
    }

    int total_bytes = cantidad * sizeof(BloquePlano);
    int enviados = send(socket, (const char*)planos, total_bytes, 0);
    if (enviados != total_bytes) {
        printf("Error al enviar los bloques planos\n");
        return -1;
    }

    return enviados;
}
/**
 * Recibe una cadena de texto desde el cliente con una acción (por ejemplo, "MOVER:L").
 */
int recibir_accion(SOCKET socket_cliente, char* buffer, int tamanio) {
    int recibidos = recv(socket_cliente, buffer, tamanio, 0);
    if (recibidos == SOCKET_ERROR) {
        printf("Error al recibir datos: %d\n", WSAGetLastError());
        return -1;
    }
    buffer[recibidos] = '\0';
    return recibidos;
}




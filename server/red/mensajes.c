#include <stdio.h>
#include <winsock2.h>
#include "../juego/mapa.h"
#include "../juego/juego.h"
#include "mensajes.h"
#include <string.h>

int enviar_juego(SOCKET socket, const Juego* juego) {
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
    }

    // Enviar variables del juego
    if (send(socket, &juego->nivel_actual, 4, 0) != 4) return -1;
    //imprimir_bytes(&juego->nivel_actual, 4, "nivel_actual");

    if (send(socket, &juego->en_fase_bonus, 4, 0) != 4) return -1;
    //imprimir_bytes(&juego->en_fase_bonus, 4, "en_fase_bonus");

    if (send(socket, &juego->velocidad, 4, 0) != 4) return -1;
    //imprimir_bytes(&juego->velocidad, 4, "velocidad");

    return 0;
}

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

int recibir_accion(SOCKET socket_cliente, char* buffer, int tamanio) {
    int recibidos = recv(socket_cliente, buffer, tamanio, 0);
    if (recibidos == SOCKET_ERROR) {
        printf("Error al recibir datos: %d\n", WSAGetLastError());
        return -1;
    }
    buffer[recibidos] = '\0';
    return recibidos;
}

/*
void imprimir_bytes(const void* data, size_t size, const char* etiqueta) {
    const unsigned char* bytes = (const unsigned char*)data;
    printf("[%s] %zu bytes: ", etiqueta, size);
    for (size_t i = 0; i < size; i++) {
        printf("%02X ", bytes[i]);
    }
    printf("\n");
}
*/


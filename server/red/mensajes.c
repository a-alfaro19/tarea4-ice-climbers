#include "mensajes.h"
#include <stdio.h>
#include <string.h>

/**
 * Nota: Este ejemplo asume que tanto el cliente como el servidor comparten la definición exacta de struct `Juego`.
 * Para algo más robusto, deberías enviar campos por separado o usar un protocolo como JSON o Protobuf.
 */

int enviar_juego(SOCKET socket_cliente, const Juego *juego) {
    int total_enviado = 0;
    const char *puntero = (const char *)juego;
    int bytes_restantes = sizeof(Juego);

    while (bytes_restantes > 0) {
        int enviados = send(socket_cliente, puntero + total_enviado, bytes_restantes, 0);
        if (enviados == SOCKET_ERROR) {
            printf("Error al enviar juego: %d\n", WSAGetLastError());
            return -1;
        }
        total_enviado += enviados;
        bytes_restantes -= enviados;
    }

    return total_enviado;
}

int recibir_accion(SOCKET socket_cliente, void *buffer, int tamanio) {
    int recibidos = recv(socket_cliente, buffer, tamanio, 0);
    if (recibidos == SOCKET_ERROR) {
        printf("Error al recibir datos: %d\n", WSAGetLastError());
        return -1;
    }
    return recibidos;
}

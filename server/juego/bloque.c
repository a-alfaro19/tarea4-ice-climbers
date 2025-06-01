// bloque.c
#include "bloque.h"

#include <stdio.h>
#include <winsock2.h>

Bloque crear_bloque(int id, int x, int y, int vida) {
    Bloque b;
    b.id = id;
    b.x = x;
    b.y = y;
    b.vida = vida;
    b.destruido = 0;
    return b;
}

int enviar_bloques(SOCKET socket_cliente, const PaqueteBloques *paquete) {
    int total_enviado = 0;
    const char *puntero = (const char *)paquete;
    int bytes_restantes = sizeof(PaqueteBloques);

    while (bytes_restantes > 0) {
        int enviados = send(socket_cliente, puntero + total_enviado, bytes_restantes, 0);
        if (enviados == SOCKET_ERROR) {
            printf("Error al enviar bloques: %d\n", WSAGetLastError());
            return -1;
        }
        total_enviado += enviados;
        bytes_restantes -= enviados;
    }

    return total_enviado;
}
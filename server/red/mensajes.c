#include <stdio.h>
#include <winsock2.h>
#include "../juego/Map.h"
#include "../juego/juego.h"
#include "mensajes.h"
#include <string.h>

// int enviar_juego(SOCKET socket, const Juego* juego) {
//     for (int i = 0; i < 2; i++) {
//         Jugador* j = &juego->jugadores[i];
//
//         // Enviar nombre (10 bytes)
//         if (send(socket, j->nombre, 10, 0) != 10) return -1;
//         //imprimir_bytes(j->nombre, 10, "nombre");
//
//         // Enviar campos
//         if (send(socket, &j->x, 4, 0) != 4) return -1;
//         //imprimir_bytes(&j->x, 4, "x");
//
//         if (send(socket, &j->y, 4, 0) != 4) return -1;
//         //imprimir_bytes(&j->y, 4, "y");
//
//         if (send(socket, &j->vidas, 4, 0) != 4) return -1;
//         //imprimir_bytes(&j->vidas, 4, "vidas");
//
//         if (send(socket, &j->puntaje, 4, 0) != 4) return -1;
//         ///imprimir_bytes(&j->puntaje, 4, "puntaje");
//
//         if (send(socket, &j->direccion, 1, 0) != 1) return -1;
//         //imprimir_bytes(&j->direccion, 1, "direccion");
//
//         char relleno = 0;
//         if (send(socket, &relleno, 1, 0) != 1) return -1;
//         //imprimir_bytes(&relleno, 1, "relleno");
//     }
//
//     // Enviar variables del juego
//     if (send(socket, &juego->nivel_actual, 4, 0) != 4) return -1;
//     //imprimir_bytes(&juego->nivel_actual, 4, "nivel_actual");
//
//     if (send(socket, &juego->en_fase_bonus, 4, 0) != 4) return -1;
//     //imprimir_bytes(&juego->en_fase_bonus, 4, "en_fase_bonus");
//
//     if (send(socket, &juego->velocidad, 4, 0) != 4) return -1;
//     //imprimir_bytes(&juego->velocidad, 4, "velocidad");
//
//     return 0;
// }

int send_map(const SOCKET socket, Tile map[TOTAL_ROWS][TILES_PER_ROW]) {
    int rows = TOTAL_ROWS;
    int cols = TILES_PER_ROW;

    printf("Map rows: %d\n", rows);
    printf("Map cols: %d\n", cols);

    // Send Map Size
    if (send(socket, (char*)&rows, sizeof(int), 0) != sizeof(int)) return 0;
    if (send(socket, (char*)&cols, sizeof(int), 0) != sizeof(int)) return 0;

    // Send Map
    if (send(socket, (char*)map, sizeof(Tile) * TOTAL_ROWS * TILES_PER_ROW, 0) != sizeof(Tile) * TOTAL_ROWS * TILES_PER_ROW) return 0;

    return 1;
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


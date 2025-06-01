#include <stdio.h>
#include <string.h>
#include <winsock2.h>

#include <stdlib.h>
#include <windows.h>

#include "red/SocketServer.h"
// #include "red/mensajes.h"
#include "juego/juego.h"
#include "util/log.h"
// #include "red/clientes.h"
// #include "red/observer.h"

#define BUFFER_SIZE 1024

int main(void) {
    inicializar_juego(&juego_global);
    // UTF8
    SetConsoleOutputCP(CP_UTF8);
    SetConsoleCP(CP_UTF8);

    // Get Server Socket Instance
    const SOCKET serverSocket = get_server_socket();
    if (serverSocket == INVALID_SOCKET) {
        return 1;
    }

    struct sockaddr_in clientAddr;
    int clientSize;

    while (1) {
        log_info("Waiting for client...");
        clientSize = sizeof(clientAddr);
        SOCKET *newSocket = malloc(sizeof(SOCKET));
        *newSocket = accept(serverSocket, (struct sockaddr*)&clientAddr, &clientSize);

        if (*newSocket == INVALID_SOCKET) {
            log_error("Failed to accept client connection.");
            free(newSocket);
            continue;
        }

        CreateThread(NULL, 0, handle_client, newSocket, 0, NULL);
    }

    close_server();
    return 0;

    // Obstaculo* obs = crear_obstaculo(YETI, 10, 10, 10, 10);
    // obs->direccion = DER;
    // printf("Posición actual x: %d, y: %d\n", obs->pos.x, obs->pos.y);
    // obs->mover(obs);
    // printf("Posición actual x: %d, y: %d\n", obs->pos.x, obs->pos.y);
    //
    // if (destruir_obstaculo(obs)) {
    //     printf("Obstáculo destruido correctamente\n");
    //     obs = NULL;
    // } else {
    //     printf("Error al destruir el obstáculo\n");
    // }
    //
    // return 0;
}

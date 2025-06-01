#include <stdio.h>
#include <string.h>
#include <winsock2.h>
#include "red/SocketServer.h"
#include "red/mensajes.h"
#include "juego/juego.h"
#include "juego/obstaculo.h"
#include "util/log.h"
#include <windows.h>

#define BUFFER_SIZE 1024

int main(void) {
    // UTF8
    SetConsoleOutputCP(CP_UTF8);
    SetConsoleCP(CP_UTF8);


    const SOCKET serverSocket = get_server_socket();
    if (serverSocket == INVALID_SOCKET) {
        return 1;
    }

    Juego juego;
    inicializar_juego(&juego);

    struct sockaddr_in clientAddr;
    int clientSize;

    while (1) {
        log_info("Esperando cliente...");
        clientSize = sizeof(clientAddr);
        const SOCKET clientSocket = accept(serverSocket, (struct sockaddr *) &clientAddr, &clientSize);

        if (clientSocket == INVALID_SOCKET) {
            log_error("Fallo al aceptar cliente.");
            continue;
        }

        log_info("Cliente conectado.");

        int conectado = 1;
        char buffer[BUFFER_SIZE];

        while (conectado) {
            int bytesRecibidos = recibir_accion(clientSocket, buffer, sizeof(buffer) - 1);

            if (bytesRecibidos <= 0) {
                log_info("Cliente desconectado.");
                conectado = 0;
                break;
            }

            buffer[bytesRecibidos] = '\0';

            log_info("Acción recibida del cliente:");
            printf(">> %s\n", buffer);

            // Comandos simples
            if (strcmp(buffer, "exit") == 0) {
                log_info("Cliente pidió salir.");
                conectado = 0;
                break;
            } else if (strcmp(buffer, "estado") == 0) {
                enviar_juego(clientSocket, &juego);
            } else if (strcmp(buffer, "reiniciar") == 0) {
                reiniciar_juego(&juego);
                enviar_juego(clientSocket, &juego);
            } else {
                // Aquí puedes mapear acciones como mover jugador o golpear
                // Por ahora solo se envía estado
                enviar_juego(clientSocket, &juego);
            }
        }

        closesocket(clientSocket);
        log_info("Conexión con cliente cerrada.");
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

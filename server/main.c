#include <stdio.h>
#include <string.h>
#include <winsock2.h>

#include <stdlib.h>
#include <windows.h>

#include "red/SocketServer.h"
// #include "red/mensajes.h"
// #include "juego/juego.h"
#include "util/log.h"
// #include "red/clientes.h"
// #include "red/observer.h"


#define BUFFER_SIZE 1024

// Juego juego;  // Variable global compartida por los hilos

// DWORD WINAPI atender_cliente(LPVOID param) {
//     SOCKET clientSocket = *((SOCKET*)param);
//     free(param);
//
//     char buffer[BUFFER_SIZE];
//     int conectado = 1;
//
//     int id_jugador = -1;
//     int es_jugador = 0;
//
//     // Esperar ID
//     int bytesRecibidos = recibir_accion(clientSocket, buffer, sizeof(buffer) - 1);
//     if (bytesRecibidos <= 0) {
//         log_info("Cliente desconectado.");
//         closesocket(clientSocket);
//         return 0;
//     }
//     buffer[bytesRecibidos] = '\0';
//
//     // Procesar ID
//     if (strcmp(buffer, "PLAYER") == 0) {
//         // Player Client
//         ClienteConectado *clientes = obtener_clientes();
//         int jugadores_actuales = 0;
//         for (int i = 0; i < total_clientes(); i++) {
//             if (clientes[i].es_jugador) jugadores_actuales++;
//         }
//
//         if (jugadores_actuales < 2) {
//             es_jugador = 1;
//             id_jugador = jugadores_actuales;
//             if (send(clientSocket, "ACEPTADO", 8, 0) < 0) {
//                 log_error("Error al enviar respuesta de aceptación");
//                 closesocket(clientSocket);
//                 return 0;
//             }
//         } else {
//             log_info("Rechazando jugador: máximo de jugadores alcanzado");
//             send(clientSocket, "RECHAZADO", 9, 0);
//             closesocket(clientSocket);
//             return 0;
//         }
//     }
//     else if (strcmp(buffer, "OBSERVER") == 0) {
//         es_jugador = 0;
//         id_jugador = -1;
//         if (send(clientSocket, "ACEPTADO", 8, 0) < 0) {
//             log_error("Error al enviar respuesta de aceptación");
//             closesocket(clientSocket);
//             return 0;
//         }
//     }
//     else {
//         log_error("Identificación no válida recibida");
//         send(clientSocket, "INVALID", 8, 0);
//         closesocket(clientSocket);
//         return 0;
//     }
//
//     // Registrar Cliente
//     if (!registrar_cliente(clientSocket, es_jugador, id_jugador)) {
//         log_info("Cliente no registrado (límite alcanzado). Cerrando conexión.");
//         closesocket(clientSocket);
//         return 0;
//     }
//
//     printf("[INFO] Cliente registrado como: %s\n",
//            es_jugador ? (id_jugador == 0 ? "Popo" : "Nana") : "Observador");
//
//     // Loop principal de la comunicación
//     Nivel *nivel_actual = &juego.niveles[juego.nivel_actual];
//     while (conectado) {
//         int bytesRecibidos = recibir_accion(clientSocket, buffer, sizeof(buffer) - 1);
//
//         if (bytesRecibidos <= 0) {
//             log_info("Cliente desconectado.");
//             conectado = 0;
//             break;
//         }
//
//         buffer[bytesRecibidos] = '\0';
//
//         log_info("Acci\xC3\xB3n recibida del cliente:");
//         printf(">> %s\n", buffer);
//
//         if (strcmp(buffer, "exit") == 0) {
//             conectado = 0;
//             break;
//
//         } else if (strcmp(buffer, "estado") == 0) {
//             enviar_juego(clientSocket, &juego);
//
//         } else if (strcmp(buffer, "reiniciar") == 0) {
//             reiniciar_juego(&juego);
//             actualizar_juego(&juego);
//
//         } else if (strncmp(buffer, "MOVER:", 6) == 0 && id_jugador != -1) {
//             char dir = buffer[6];
//             mover_jugador(&juego.jugadores[id_jugador], dir);
//             actualizar_juego(&juego);
//
//         } else if (strcmp(buffer, "BRINCAR") == 0 && id_jugador != -1) {
//             brincar_jugador(&juego.jugadores[id_jugador]);
//             actualizar_juego(&juego);
//
//         } else if (strcmp(buffer, "GOLPEAR") == 0 && id_jugador != -1) {
//             golpear(&juego.jugadores[id_jugador], nivel_actual);
//             actualizar_juego(&juego);
//
//         } else {
//             log_info("Comando no reconocido. Enviando estado.");
//         }
//
//         notificar_observers(&juego);  // Enviar a todos
//     }
//
//     remover_cliente(clientSocket);
//     closesocket(clientSocket);
//     log_info("Conexi\xC3\xB3n con cliente cerrada.");
//     return 0;
// }

int main(void) {
    // UTF8
    SetConsoleOutputCP(CP_UTF8);
    SetConsoleCP(CP_UTF8);

    // Get Server Socket Instance
    const SOCKET serverSocket = get_server_socket();
    if (serverSocket == INVALID_SOCKET) {
        return 1;
    }

    // inicializar_juego(&juego);

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

#include "SocketServer.h"
#include <stdio.h>
#include <ws2tcpip.h>
#include <string.h>
#include <stdlib.h>
#include <windows.h>

#include "clientes.h"
#include "../juego/nivel.h"
#include "../juego/bloque.h"
#include "../juego/mapa.h"
#include "../juego/juego.h"
#include "../juego/jugador.h"
#include "mensajes.h"
#include "../juego/acciones.h"
#pragma comment(lib, "ws2_32.lib")

#define PORT 8080
#define BUFFER_SIZE 1024
#define MAX_CLIENTS 6

static ClientInfo clients[MAX_CLIENTS];
static int num_clients = 0;
static int player_clients = 0;
static int observer_clients = 0;
static Juego juego;
static ModoJuego modo_actual = SIN_PARTIDA;

int initialize_winsock() {
    static int initialized = 0;
    static WSADATA wsaData;

    if (!initialized) {
        // printf("Initialising Winsock...\n");
        if (WSAStartup(MAKEWORD(2, 2), &wsaData) != 0) {
            printf("WSAStartup failed: %d\n", WSAGetLastError());
            return 0;
        }
        initialized = 1;
        // printf("Winsock initialized correctly\n");
    }
    return 1;
}

SOCKET get_server_socket() {
    static SOCKET serverSocket = INVALID_SOCKET;

    if (serverSocket != INVALID_SOCKET) return serverSocket;

    if (!initialize_winsock()) return INVALID_SOCKET;

    struct sockaddr_in serverAddr;
    serverSocket = socket(AF_INET, SOCK_STREAM, 0);
    if (serverSocket == INVALID_SOCKET) {
        printf("Error creating socket: %d\n", WSAGetLastError());
        return INVALID_SOCKET;
    }

    serverAddr.sin_family = AF_INET;
    serverAddr.sin_addr.S_un.S_addr = INADDR_ANY;
    serverAddr.sin_port = htons(PORT);

    if (bind(serverSocket, (struct sockaddr*)&serverAddr, sizeof(serverAddr)) == SOCKET_ERROR) {
        printf("Error binding socket: %d\n", WSAGetLastError());
        closesocket(serverSocket);
        return INVALID_SOCKET;
    }

    if (listen(serverSocket, SOMAXCONN) == SOCKET_ERROR) {
        printf("Error listening on socket: %d\n", WSAGetLastError());
        closesocket(serverSocket);
        return INVALID_SOCKET;
    }

    printf("Server socket created and listening on port %d\n", PORT);
    return serverSocket;
}

void close_server() {
    const SOCKET serverSocket = get_server_socket();
    if (serverSocket != INVALID_SOCKET) {
        closesocket(serverSocket);
        WSACleanup();
        printf("Server closed\n");
    }
}

int receive_request(const SOCKET socket, char *buffer, const int buffer_size) {
    const int bytes_received = recv(socket, buffer, buffer_size, 0);
    if (bytes_received <= 0) {
        return 0;
    }
    buffer[bytes_received] = '\0';
    return 1;
}

int send_response(const SOCKET socket, const char *response) {
    const int bytes_sent = send(socket, response, (int)strlen(response), 0);
    return (bytes_sent > 0);
}

// Game loop global
DWORD WINAPI game_loop(LPVOID param) {
    (void)param;

    while (1) {
        actualizar_juego(&juego, mapa);

        for (int i = 0; i < num_clients; i++) {
            if (clients[i].type == PLAYER || clients[i].type == OBSERVER) {
                if (enviar_juego(clients[i].socket, &juego) < 0) continue;
                PaqueteBloques paquete = obtener_bloques_visibles();
                enviar_bloques(clients[i].socket, &paquete);
            }
        }

        Sleep(50);  // 20 FPS
    }

    return 0;
}

DWORD WINAPI consoleThread(LPVOID lpParameter) {
    Juego* game = lpParameter;
    char command[1024];

    while (1) {
        printf(">> ");
        fflush(stdout);

        if (fgets(command, sizeof(command), stdin) == NULL) continue;

        // Remove line jmp
        command[strcspn(command, "\n")] = 0;

        char typeStr[32];

        if (sscanf(command, "%s", typeStr) == 1) {
            const ObstacleType type = parse_obstacle_type(typeStr);
            generate_obstacle(game, type);
        }

    }
}

DWORD WINAPI handle_client(LPVOID param) {
    const SOCKET clientSocket = *(SOCKET*)param;
    free(param);

    char buffer[BUFFER_SIZE];
    ClientInfo *client = NULL;

    if (!receive_request(clientSocket, buffer, sizeof(buffer) - 1)) {
        printf("Error receiving ID from client\n");
        closesocket(clientSocket);
        return 0;
    }
    buffer[strcspn(buffer, "\r\n")] = '\0';
    if (strcmp(buffer, "PLAYER1") == 0) {
        if (modo_actual == SIN_PARTIDA) {
            client = &clients[num_clients++];
            client->socket = clientSocket;
            client->type = PLAYER;
            client->id = player_clients++;
            modo_actual = MODO_UNO_JUGADOR;

            send_response(clientSocket, "ACCEPTED\n");
            const char* nombre = "Popo      ";
            send(clientSocket, nombre, 10, 0);
            printf("Jugador Popo conectado (modo 1 jugador)\n");

        } else {
            send_response(clientSocket, "REJECTED\n");
            closesocket(clientSocket);
            printf("Conexión rechazada: ya hay una partida activa\n");
            return 0;
        }

        } else if (strcmp(buffer, "PLAYER2") == 0) {
            if (modo_actual == SIN_PARTIDA) {
                client = &clients[num_clients++];
                client->socket = clientSocket;
                client->type = PLAYER;
                client->id = player_clients++;
                modo_actual = MODO_DOS_JUGADORES;


                send_response(clientSocket, "ACCEPTED\n");
                const char* nombre = "Popo      ";
                send(clientSocket, nombre, 10, 0);
                printf("Jugador Popo conectado (modo 2 jugadores)\n");

            } else if (modo_actual == MODO_DOS_JUGADORES && player_clients == 1) {
                client = &clients[num_clients++];
                client->socket = clientSocket;
                client->type = PLAYER;
                client->id = player_clients++;

                send_response(clientSocket, "ACCEPTED\n");
                const char* nombre = "Nana      ";
                send(clientSocket, nombre, 10, 0);
                printf("Jugador Nana conectado (modo 2 jugadores)\n");

                // Avisar a Popo que puede iniciar
                for (int i = 0; i < num_clients; i++) {
                    if (clients[i].type == PLAYER && clients[i].id == 0) {
                        send_response(clients[i].socket, "START\n");
                        break;
                    }
                }
        } else {
            send_response(clientSocket, "REJECTED\n");
            closesocket(clientSocket);
            printf("Conexión rechazada: no se puede unir a modo 2 jugadores\n");
            return 0;
        }
    }  else if (
        strcmp(buffer, "OBSERVER") == 0 ||
        strcmp(buffer, "OBSERVER_POPO") == 0 ||
        strcmp(buffer, "OBSERVER_NANA") == 0
    ) {
        int observando_a = -1;

        int popo_count = contar_observadores_de(0);
        int nana_count = contar_observadores_de(1);

        if (modo_actual == SIN_PARTIDA) {
            send_response(clientSocket, "REJECTED\n");
            closesocket(clientSocket);
            printf("Conexión rechazada: no hay partida activa\n");
            return 0;
        }

        if (modo_actual == MODO_UNO_JUGADOR) {
            if (strcmp(buffer, "OBSERVER_NANA") == 0) {
                send_response(clientSocket, "REJECTED\n");
                closesocket(clientSocket);
                printf("Rechazado: no se puede observar a Nana en modo 1 jugador\n");
                return 0;
            }
            if (popo_count >= 2) {
                send_response(clientSocket, "REJECTED\n");
                closesocket(clientSocket);
                printf("Rechazado: límite de observadores para Popo (modo 1 jugador)\n");
                return 0;
            }
            observando_a = 0;

        } else if (modo_actual == MODO_DOS_JUGADORES) {
            if (strcmp(buffer, "OBSERVER_POPO") == 0) {
                if (popo_count >= 2) {
                    send_response(clientSocket, "REJECTED\n");
                    closesocket(clientSocket);
                    printf("Rechazado: límite de observadores para Popo\n");
                    return 0;
                }
                observando_a = 0;
            } else if (strcmp(buffer, "OBSERVER_NANA") == 0) {
                if (nana_count >= 2) {
                    send_response(clientSocket, "REJECTED\n");
                    closesocket(clientSocket);
                    printf("Rechazado: límite de observadores para Nana\n");
                    return 0;
                }
                observando_a = 1;
            } else {
                send_response(clientSocket, "REJECTED\n");
                closesocket(clientSocket);
                printf("Rechazado: debe especificar OBSERVER_POPO o OBSERVER_NANA\n");
                return 0;
            }
        }

        if (!registrar_cliente(clientSocket, 0, -1)) {
            send_response(clientSocket, "REJECTED\n");
            closesocket(clientSocket);
            printf("Error al registrar observador en estructura global\n");
            return 0;
        }

        ClienteConectado* lista = obtener_clientes();
        lista[total_clientes() - 1].observando_a = observando_a;

        client = &clients[num_clients++];
        client->socket = clientSocket;
        client->type = OBSERVER;
        client->id = observer_clients++;

        if (!send_response(clientSocket, "ACCEPTED\n")) {
            closesocket(clientSocket);
            return 0;
        }

        unsigned char bytes[4];
        bytes[0] = observando_a & 0xFF;
        bytes[1] = (observando_a >> 8) & 0xFF;
        bytes[2] = (observando_a >> 16) & 0xFF;
        bytes[3] = (observando_a >> 24) & 0xFF;
        send(clientSocket, (const char*)bytes, 4, 0);

        // Envia byte indicando si el juego es de 2 jugadores
        unsigned char modo_byte = (modo_actual == MODO_DOS_JUGADORES) ? 1 : 0;
        send(clientSocket, (const char*)&modo_byte, 1, 0);
        
        printf("Observer Client accepted (observando a %s)\n", observando_a == 0 ? "Popo" : "Nana");
        printf("Observadores actuales: Popo = %d, Nana = %d\n", contar_observadores_de(0), contar_observadores_de(1));
    }


    // Enviar estado inicial
    if (enviar_juego(clientSocket, &juego) < 0) return 0;
    PaqueteBloques paquete = obtener_bloques_visibles();
    if (enviar_bloques(clientSocket, &paquete) < 0) return 0;

    // Escucha comandos del cliente
    while (1) {
        if (!receive_request(clientSocket, buffer, sizeof(buffer) - 1)) break;

        buffer[strcspn(buffer, "\r\n")] = '\0';

        if (strcmp(buffer, "STATE") == 0) {
            snprintf(buffer, sizeof(buffer), "STATE %d %d", player_clients, observer_clients);
            send_response(clientSocket, buffer);
        } else if (
            strncmp(buffer, "MOVER:", 6) == 0 ||
            strcmp(buffer, "BRINCAR") == 0 ||
            strcmp(buffer, "GOLPEAR") == 0
        ) {
            if (client->type == PLAYER) {
                Jugador* jug = &juego.jugadores[client->id];
                if (strncmp(buffer, "MOVER:", 6) == 0) {
                    mover_jugador(jug, buffer[6]);
                } else if (strcmp(buffer, "BRINCAR") == 0) {
                    brincar_jugador(jug);
                } else if (strcmp(buffer, "GOLPEAR") == 0) {
                    golpear(jug, mapa);
                }
            }
            printf("Acción recibida: %s\n", buffer);
        } else {
            printf("Comando no reconocido: %s\n", buffer);
        }
    }

    printf("Client disconnected\n");
    closesocket(clientSocket);
    remover_cliente(clientSocket);

    if (client->type == PLAYER && player_clients > 0) player_clients--;
    if (client->type == OBSERVER && observer_clients > 0) observer_clients--;
    if (num_clients > 0) num_clients--;

    if (player_clients == 0) {
        modo_actual = SIN_PARTIDA;
        printf("Modo de juego reiniciado a SIN_PARTIDA\n");

        // Reiniciar juego y mapa
        inicializar_mapa();
        inicializar_juego(&juego);

        observer_clients = 0;
        num_clients = 0;

        return 0;
    }

}

int main(void) {
    inicializar_mapa();
    inicializar_juego(&juego);
    SetConsoleOutputCP(CP_UTF8);
    SetConsoleCP(CP_UTF8);

    //  Lanza el loop global del juego
    CreateThread(NULL, 0, game_loop, NULL, 0, NULL);
    CreateThread(NULL, 0, consoleThread, &juego, 0, NULL);

    const SOCKET serverSocket = get_server_socket();
    if (serverSocket == INVALID_SOCKET) return 1;

    struct sockaddr_in clientAddr;
    int clientSize;

    while (1) {
        printf("Waiting for client...\n");
        clientSize = sizeof(clientAddr);
        SOCKET *newSocket = malloc(sizeof(SOCKET));
        *newSocket = accept(serverSocket, (struct sockaddr*)&clientAddr, &clientSize);

        if (*newSocket == INVALID_SOCKET) {
            printf("Failed to accept client connection.\n");
            free(newSocket);
            continue;
        }

        CreateThread(NULL, 0, handle_client, newSocket, 0, NULL);
    }

    close_server();
    return 0;
}

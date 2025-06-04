
#include "SocketServer.h"
#include <stdio.h>
#include <ws2tcpip.h>
#include <string.h>
#include <stdlib.h>
#include <windows.h>
#include "mensajes.h"
#include "../juego/Map.h"
#include "../juego/juego.h"
#include "../juego/jugador.h"
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

int initialize_winsock() {
    static int initialized = 0;
    static WSADATA wsaData;

    if (!initialized) {
        printf("Initialising Winsock...\n");
        if (WSAStartup(MAKEWORD(2, 2), &wsaData) != 0) {
            printf("WSAStartup failed: %d\n", WSAGetLastError());
            return 0;
        }
        initialized = 1;
        printf("Winsock initialized correctly\n");
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

int receive_Int_BE(const SOCKET socket, int *len) {
    unsigned char bytes[4];
    const int received = recv(socket, bytes, 4, 0);
    if (received != 4) {
        printf("Error receiving data len (received %d bytes).\n", received);
        return 0;
    }

    *len = bytes[0] << 24 | bytes[1] << 16 | bytes[2] << 8 | bytes[3];
    return 1;
}

int receive_message(const SOCKET socket, char *buffer, const int buffer_size) {
    int length = 0;

    // Receive len first
    if (!receive_Int_BE(socket, &length)) return 0;

    // Validate request size
    printf("Received length: %d\n", length);
    printf("Received buffer size: %d\n", buffer_size);
    if (length<= 0 || length >= buffer_size) {
        printf("Buffer overflow.\n");
        return 0;
    }

    // Receive request
    const int received = recv(socket, buffer, length, 0);
    if (received != length) {
        printf("Error receiving data.\n");
        return 0;
    }

    buffer[length] = '\0';
    printf("Received %d bytes: %s\n", received, buffer);
    return 1;
}

int send_message(const SOCKET socket, const char *response) {
    // Get response len
    int len = (int) strlen(response);

    // Send len first
    int bytes_sent = send(socket, (char*)&len, sizeof(int), 0);
    if (bytes_sent <= 0) {
        printf("Error sending response length: %d\n", WSAGetLastError());
        return 0;
    }

    // Send response string
    bytes_sent = send(socket, response, len, 0);
    if (bytes_sent <= 0) {
        printf("Error sending response body: %d\n", WSAGetLastError());
        return 0;
    }

    return 1;
}

DWORD WINAPI handle_client(LPVOID param) {
    const SOCKET clientSocket = *(SOCKET*)param;
    free(param);

    char buffer[BUFFER_SIZE];
    ClientInfo *client = NULL;

    // Receive Client ID
    if (!receive_message(clientSocket, buffer, sizeof(buffer) - 1)) {
        closesocket(clientSocket);
        return 0;
    }

    // Process ID
    if (strcmp(buffer, "PLAYER") == 0) {
        if (player_clients < 2) {
            // Register Client
            client = &clients[num_clients++];
            client->socket = clientSocket;
            client->type = PLAYER;
            client->id = player_clients++;

            // Send Approval
            if (!send_message(clientSocket, "ACCEPTED\n")) {
                closesocket(clientSocket);
                return 0;
            }

            printf("Player Client accepted\n");

        } else {
            // Send Rejection
            printf("Player Client rejected: maximum number of players reached\n");
            send_message(clientSocket, "REJECTED\n");
            closesocket(clientSocket);
            return 0;
        }
    } else if (strcmp(buffer, "OBSERVER") == 0) {
        if (observer_clients < 2) {
            // Register Client
            client = &clients[num_clients++];
            client->socket = clientSocket;
            client->type = OBSERVER;
            client->id = observer_clients++;

            // Send Approval
            if (!send_message(clientSocket, "ACCEPTED\n")) {
                closesocket(clientSocket);
                return 0;
            }

            printf("Observer Client accepted\n");

        } else {
            // Send Rejection
            printf("Client rejected: maximum number of observers reached\n");
            send_message(clientSocket, "REJECTED\n");
            closesocket(clientSocket);
            return 0;
        }
    } else {
        printf("Invalid ID received from client\n");
        closesocket(clientSocket);
        return 0;
    }

    // Send Data
    if (client->type == PLAYER) {
        // Send Player Name
        const char* name = (client->id == 0) ? "Popo" : "Nana";
        if (!send_message(clientSocket, name)) {
            closesocket(clientSocket);
            return 0;
        }
    }

    // Send Game Data
    // if (enviar_juego(client->socket, &juego) < 0) {
    //     printf("Error al enviar el juego\n");
    //     return 0;
    // }

    // Send Map
    if (!send_map(client->socket, map)) {
        printf("Error sending Map\n");
        return 0;
    }

    while (1) {
        if (!receive_message(clientSocket, buffer, sizeof(buffer) - 1)) break;

        buffer[strcspn(buffer, "\r\n")] = '\0';

        if (strcmp(buffer, "STATE") == 0) {
            snprintf(buffer, sizeof(buffer), "STATE %d %d", player_clients, observer_clients);
            send_message(clientSocket, buffer);
        } else if (
            strncmp(buffer, "MOVE:", 5) == 0 ||
            strcmp(buffer, "JUMP") == 0 ||
            strcmp(buffer, "GOLPEAR") == 0
        ) {
            if (client->type == PLAYER) {
                Jugador* jug = &juego.jugadores[client->id];
                if (strncmp(buffer, "MOVE:", 6) == 0) {
                    // mover_jugador(jug, buffer[6]);
                } else if (strcmp(buffer, "JUMP") == 0) {
                    // brincar_jugador(jug);
                }
                // else if (strcmp(buffer, "GOLPEAR") == 0) {
                //     golpear(jug, map);
                // }

                // actualizar_juego(&juego, map);

                // if (enviar_juego(client->socket, &juego) < 0) {
                //     printf("Error al reenviar juego\n");
                //     return 0;
                // }

                if (!send_map(client->socket, map)) {
                    printf("Error sending Map\n");
                    return 0;
                }
            }

            printf("Acción recibida: %s\n", buffer);
        } else {
            printf("Comando no reconocido: %s\n", buffer);
        }
    }

    printf("Client disconnected\n");
    closesocket(clientSocket);

    if (client->type == PLAYER && player_clients > 0) player_clients--;
    if (client->type == OBSERVER && observer_clients > 0) observer_clients--;
    if (num_clients > 0) num_clients--;

    return 0;
}



int main(void) {
    SetConsoleOutputCP(CP_UTF8);
    SetConsoleCP(CP_UTF8);

    const SOCKET serverSocket = get_server_socket();
    if (serverSocket == INVALID_SOCKET) return 1;

    struct sockaddr_in clientAddr;
    int clientSize;

    // Initialize Game
    initialize_map();
    print_map();
    // inicializar_juego(&juego);

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

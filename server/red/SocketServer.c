#include "SocketServer.h"
#include <stdio.h>
#include <ws2tcpip.h>
#include "../util/log.h"
#include "../util/constantes.h"

#pragma comment(lib, "ws2_32.lib")

#define PORT 8080
#define BUFFER_SIZE 1024
#define MAX_CLIENTS 6

static ClientInfo clients[MAX_CLIENTS];
static int total_clients = 0;
static int player_clients = 0;
static int observer_clients = 0;

int initialize_winsock() {
    static int initialized = 0;
    static WSADATA wsaData;

    if (!initialized) {
        log_info("Initialising Winsock...");
        if (WSAStartup(MAKEWORD(2, 2), &wsaData) != 0) {
            char msg[256];
            snprintf(msg, sizeof(msg), "WSAStartup failed: %d", WSAGetLastError());
            log_error(msg);
            return 0;
        }
        initialized = 1;
        log_info("Winsock initialized correctly");
    }
    return 1;
}

SOCKET get_server_socket() {
    static SOCKET serverSocket = INVALID_SOCKET;
    char msg[256];

    if (serverSocket != INVALID_SOCKET) return serverSocket;

    if (!initialize_winsock()) return INVALID_SOCKET;

    // Create Socket
    serverSocket = socket(AF_INET, SOCK_STREAM, 0);
    if (serverSocket == INVALID_SOCKET) {
        snprintf(msg, sizeof(msg), "Error creating socket: %d", WSAGetLastError());
        log_error(msg);
        return INVALID_SOCKET;
    }

    // Set Timeout for reception
    DWORD timeout = SOCKET_TIMEOUT;
    if (setsockopt(serverSocket, SOL_SOCKET, SO_RCVTIMEO, (char*)&timeout, sizeof(timeout)) < 0) {
        log_error("Error setting receive timeout");
        close_server(serverSocket);
        return INVALID_SOCKET;
    }

    // Set Timeout for transmission
    if (setsockopt(serverSocket, SOL_SOCKET, SO_SNDTIMEO, (char*)&timeout, sizeof(timeout)) < 0) {
        log_error("Error setting send timeout");
        close_server(serverSocket);
        return INVALID_SOCKET;
    }

    // Setup server address
    struct sockaddr_in serverAddr;
    serverAddr.sin_family = AF_INET;
    serverAddr.sin_addr.S_un.S_addr = INADDR_ANY;
    serverAddr.sin_port = htons(PORT);

    // Bind
    if (bind(serverSocket, (struct sockaddr*)&serverAddr, sizeof(serverAddr)) == SOCKET_ERROR) {
        snprintf(msg, sizeof(msg), "Error binding socket: %d", WSAGetLastError());
        log_error(msg);
        close_server(serverSocket);
        serverSocket = INVALID_SOCKET;
        return INVALID_SOCKET;
    }

    // Listen
    if (listen(serverSocket, SOMAXCONN) == SOCKET_ERROR) {
        snprintf(msg, sizeof(msg), "Error listening on socket: %d", WSAGetLastError());
        log_error(msg);
        close_server(serverSocket);
        serverSocket = INVALID_SOCKET;
        return INVALID_SOCKET;
    }

    snprintf(msg, sizeof(msg), "Server socket created and listening on port %d", PORT);
    log_info(msg);
    return serverSocket;
}

void close_server() {
    const SOCKET serverSocket = get_server_socket();
    if (serverSocket != INVALID_SOCKET) {
        closesocket(serverSocket);
        WSACleanup();
        log_info("Server closed");
    }
}

int receive_request(const SOCKET socket, char *buffer, const int buffer_size) {
    const int bytes_received = recv(socket, buffer, buffer_size, 0);

    if (bytes_received == SOCKET_ERROR) {
        const int error = WSAGetLastError();
        if (error == WSAETIMEDOUT) {
            log_info("Client connection timed out");
        } else if (error == WSAECONNRESET) {
            log_info("Client disconnected");
        } else {
            char msg[256];
            snprintf(msg, sizeof(msg), "Error receiving request: %d", WSAGetLastError());
            log_error(msg);
        }
        return 0;
    }
    buffer[bytes_received] = '\0';

    return 1;
}

int send_response(const SOCKET socket, const char *response) {
    const int bytes_sent = send(socket, response, (int)strlen(response), 0);

    if (bytes_sent == SOCKET_ERROR) {
        const int error = WSAGetLastError();
        if (error == WSAETIMEDOUT) {
            log_info("Client connection timed out");
        } else {
            char msg[256];
            snprintf(msg, sizeof(msg), "Error sending response: %d", WSAGetLastError());
            log_error(msg);
        }
        return 0;
    }

    return 1;
}

// int send_game(const SOCKET clientSocket, const Juego *game) {
//     int total_sent = 0;
//     const char *buffer = (const char *)game;
//     int remaining_bytes = sizeof(Juego);
//
//     while (remaining_bytes > 0) {
//         const int bytes_sent = send(clientSocket, buffer + total_sent, remaining_bytes, 0);
//         if (bytes_sent == SOCKET_ERROR) {
//             char msg[256];
//             snprintf(msg, sizeof(msg), "Error sending game: %d", WSAGetLastError());
//             log_error(msg);
//             return -1;
//         }
//         total_sent += bytes_sent;
//         remaining_bytes -= bytes_sent;
//     }
//
//     return total_sent;
// }

ClientInfo* registerClient(const SOCKET clientSocket, const ClientType type) {
    // Add Client to the list
    ClientInfo* client = &clients[total_clients++];

    // Set Client Info
    client->socket = clientSocket;
    client->type = type;
    client->id = type == PLAYER ? player_clients++ : observer_clients++;

    char msg[256];
    snprintf(msg, sizeof(msg), "Client %s registered", type == PLAYER ? "Player" : "Observer");
    log_info(msg);

    return client;
}

void unregisterClient(ClientInfo* client) {
    if (client == NULL) return;

    // Update Client Count
    if (client->type == PLAYER) {
        player_clients--;
    } else {
        observer_clients--;
    }

    // Close Client Connection
    closesocket(client->socket);

    // Shift Client's List to the left
    for (int i = client->id; i < total_clients - 1; i++) {
        if (&clients[i] == client) {
            memmove(&clients[i], &clients[i + 1],
                (total_clients - i - 1) * sizeof(ClientInfo));
            break;
        }
    }

    // Reduce list size
    total_clients--;

    char msg[256];
    snprintf(msg, sizeof(msg), "Client %s unregistered", client->type == PLAYER ? "Player" : "Observer");
    log_info(msg);
}

// void notify_observers(const Juego *game) {
//     for (int i = 0; i < total_clients; i++) {
//         send_game(clients[i].socket, game);
//     }
// }

DWORD WINAPI handle_client(LPVOID param) {
    const SOCKET clientSocket = *(SOCKET*)param;
    free(param);

    char buffer[BUFFER_SIZE];
    ClientInfo *client = NULL;

    // Receive ID from Client
    if (!receive_request(clientSocket, buffer, sizeof(buffer) - 1)) {
        log_error("Error receiving ID from client\n");
        closesocket(clientSocket);
        return 0;
    }

    // Process ID
    if (strcmp(buffer, "PLAYER") == 0) {
        // Player Client
        if (player_clients < 2) {
            // Send acceptance
            if (!send_response(clientSocket, "ACCEPTED")) {
                closesocket(clientSocket);
                return 0;
            }

            // Register Client
            client = registerClient(clientSocket, PLAYER);
        } else {
            log_info("Player Client rejected: maximum number of players reached");
            send_response(clientSocket, "REJECTED");
            closesocket(clientSocket);
            return 0;
        }
    }
    else if (strcmp(buffer, "OBSERVER") == 0) {
        // Observer Client
        if (observer_clients < 2) {
            // Send acceptance
            if (!send_response(clientSocket, "ACCEPTED")) {
                closesocket(clientSocket);
                return 0;
            }

            // Register Client
            client = registerClient(clientSocket, PLAYER);
        } else {
            log_info("Client rejected: maximum number of observers reached");
            send_response(clientSocket, "REJECTED");
            closesocket(clientSocket);
            return 0;
        }
    }
    else {
        log_error("Invalid ID received from client");
        closesocket(clientSocket);
        return 0;
    }

    char msg[256];

    // Start Communication Loop
    while (client != NULL) {
        if (!receive_request(clientSocket, buffer, sizeof(buffer) - 1)) {
            unregisterClient(client);
            break;
        }

        // Client sent a request
        if (strcmp(buffer, "") != 0) {
            if (strcmp(buffer, "EXIT") == 0) {
                // End Communication
                break;

            } else if (strcmp(buffer, "STATE") == 0) {
                // Send Game State
                // send_game(clientSocket, &juego);
                send_response(clientSocket, "OK");

            }
            // else if (strcmp(buffer, "RESTART") == 0) {
            //     // Restart Game
            //     reiniciar_juego(&juego);
            //     actualizar_juego(&juego);
            //
            // } else if (strncmp(buffer, "MOVE:", 6) == 0 && id_jugador != -1) {
            //     // Move Character
            //     char dir = buffer[6];
            //     mover_jugador(&juego.jugadores[id_jugador], dir);
            //     actualizar_juego(&juego);
            //
            // } else if (strcmp(buffer, "JUMP") == 0 && id_jugador != -1) {
            //     // Jump
            //     brincar_jugador(&juego.jugadores[id_jugador]);
            //     actualizar_juego(&juego);
            //
            // } else if (strcmp(buffer, "HIT") == 0 && id_jugador != -1) {
            //     // Hit
            //     golpear(&juego.jugadores[id_jugador], nivel_actual);
            //     actualizar_juego(&juego);
            //
            // }

            else {
                snprintf(msg, sizeof(msg), "Invalid action received from client: %s", buffer);
                log_error(msg);
            }
        }

        //         notify_observers(&juego);  // Enviar a todos
    }

    // Remove Client
    unregisterClient(client);

    return 0;
}

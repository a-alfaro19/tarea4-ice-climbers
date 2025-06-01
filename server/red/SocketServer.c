#include "SocketServer.h"
#include <stdio.h>
#include <ws2tcpip.h>
#include "../util/log.h"

#pragma comment(lib, "ws2_32.lib")

#define PORT 8080
#define BUFFER_SIZE 1024
#define MAX_CLIENTS 6

static ClientInfo clients[MAX_CLIENTS];
static int num_clients = 0;
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

    struct sockaddr_in serverAddr;

    // Create Socket
    serverSocket = socket(AF_INET, SOCK_STREAM, 0);
    if (serverSocket == INVALID_SOCKET) {
        snprintf(msg, sizeof(msg), "Error creating socket: %d", WSAGetLastError());
        log_error(msg);
        return INVALID_SOCKET;
    }

    // Setup server address
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
    if (bytes_received <= 0) {
        char msg[256];
        snprintf(msg, sizeof(msg), "Error receiving request: %d", WSAGetLastError());
        log_error(msg);
        return 0;
    }
    buffer[bytes_received] = '\0';
    return 1;
}

int send_response(const SOCKET socket, const char *response) {
    const int bytes_sent = send(socket, response, (int)strlen(response), 0);
    if (bytes_sent <= 0) {
        char msg[256];
        snprintf(msg, sizeof(msg), "Error sending response: %d", WSAGetLastError());
        log_error(msg);
        return 0;
    }
    return 1;
}

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
            // Create client info
            client = &clients[num_clients++];
            client->socket = clientSocket;
            client->type = PLAYER;
            client->id = player_clients++;

            // Send acceptance
            if (!send_response(clientSocket, "ACCEPTED")) {
                closesocket(clientSocket);
                return 0;
            }

            log_info("Player Client accepted");
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
            // Create client info
            client = &clients[num_clients++];
            client->socket = clientSocket;
            client->type = OBSERVER;
            client->id = observer_clients++;

            // Send acceptance
            if (!send_response(clientSocket, "ACCEPTED")) {
                closesocket(clientSocket);
                return 0;
            }

            log_info("Observer Client accepted");
        } else {
            log_info("Client rejected: maximum number of observers reached");
            send_response(clientSocket, "REJECTED");
            closesocket(clientSocket);
            return 0;
        }
    }
    else {
        log_error("Invalid ID received from client");
    }

    char msg[256];

    // Start Communication Loop
    while (client != NULL) {
        if (!receive_request(clientSocket, buffer, sizeof(buffer) - 1)) {
            break;
        }

        if (strcmp(buffer, "STATE") == 0) {
            // Send State
            snprintf(msg, sizeof(msg), "STATE %d %d", player_clients, observer_clients);
            if (!send_response(clientSocket, msg)) {
                log_error("Error sending state to client");
            }
        }

        closesocket(clientSocket);
        log_info("Client disconnected");
        break;
    }

    return 0;
}

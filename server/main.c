#include <stdio.h>
#include <winsock2.h>
#include <ws2tcpip.h>
#include <conio.h>
#pragma comment(lib, "ws2_32.lib")

#define PORT 8080
#define BUFFER_SIZE 1024

int main(void) {
    // Winsock variables
    WSADATA wsaData;
    SOCKET serverSocket, clientSocket;
    struct sockaddr_in serverAddr, clientAddr;
    char buffer[BUFFER_SIZE];
    int clientSize;

    // Start Winsock
    printf("Initialising Winsock...\n");
    if (WSAStartup(MAKEWORD(2, 2), &wsaData) != 0) {
        printf("Failed. Error Code: %d", WSAGetLastError());
        return 1;
    }
    printf("Winsock initialized correctly\n");

    // Create server socket
    printf("Creating Server Socket...\n");
    if ((serverSocket = socket(AF_INET, SOCK_STREAM, 0)) == INVALID_SOCKET) {
        printf("Could not create socket: %d", WSAGetLastError());
        return 1;
    }
    printf("Server socket created correctly\n");

    // Setup server address
    serverAddr.sin_family = AF_INET;
    serverAddr.sin_addr.S_un.S_addr = INADDR_ANY;
    serverAddr.sin_port = htons(PORT);

    // Bind socket server to address
    printf("Binding Server Socket to address...\n");
    if (bind(serverSocket, (struct sockaddr*)&serverAddr, sizeof(serverAddr)) == SOCKET_ERROR) {
        printf("Bind failed with error code: %d", WSAGetLastError());
        closesocket(serverSocket);
        WSACleanup();
        return 1;
    }
    printf("Socket bound correctly\n");

    // Listen to incoming connections
    if (listen(serverSocket, SOMAXCONN) == SOCKET_ERROR) {
        printf("Listen failed with error code: %d", WSAGetLastError());
        closesocket(serverSocket);
        WSACleanup();
        return 1;
    }
    printf("Listening for connections at port %d...\n", PORT);

    // Server loop
    while (1) {
        clientSize = sizeof(clientAddr);
        clientSocket = accept(serverSocket, (struct sockaddr*)&clientAddr, &clientSize);

        if (clientSocket == INVALID_SOCKET) {
            printf("Accept failed: %d\n", WSAGetLastError());
            continue;
        }
        printf("New client connected\n");

        // Receive and respond in a loop
        while (1) {
            int bytesReceived = recv(clientSocket, buffer, BUFFER_SIZE - 1, 0);
            if (bytesReceived <= 0) {
                printf("Client disconnected or error.\n");
                break;
            }

            buffer[bytesReceived] = '\0';
            printf("Received: %s\n", buffer);

            const char* response = "Hello, World!\n";
            send(clientSocket, response, (int)strlen(response), 0);
            printf("Sent: %s\n", response);
        }

        closesocket(clientSocket);
    }

    // Cleaning
    closesocket(serverSocket);
    WSACleanup();
    printf("Server closed\n");

    return 0;
}
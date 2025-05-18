#include <stdio.h>
#include <winsock2.h>
#include <conio.h>
#include <stdbool.h>
#pragma comment(lib, "ws2_32.lib")

#define PORT 8080
#define BUFFER_SIZE 1024

int main(void) {
    // Winsock variables
    WSADATA wsaData;
    SOCKET clientSocket;
    struct sockaddr_in serverAddr, clientAddr;
    int clientSize = sizeof(clientSocket);
    char buffer[BUFFER_SIZE];
    bool running = true;

    // Start Winsock
    if (WSAStartup(MAKEWORD(2, 2), &wsaData) != 0) {
        printf("Error: Winsock initialization failed\n");
        return 1;
    }
    printf("Winsock initialized correctly\n");

    // Create socket
    const SOCKET serverSocket = socket(AF_INET, SOCK_STREAM, 0);
    if (serverSocket == INVALID_SOCKET) {
        printf("Error: Socket creation failed");
        WSACleanup();
        return 1;
    }

    printf("Socket created correctly\n");

    // Setup server address
    serverAddr.sin_family = AF_INET;
    serverAddr.sin_addr.S_un.S_addr = INADDR_ANY;
    serverAddr.sin_port = htons(8000);

    // Bind socket to address
    if (bind(serverSocket, (struct sockaddr*)&serverAddr, sizeof(serverAddr)) == SOCKET_ERROR) {
        printf("Error: Socket bind failed\n");
        closesocket(serverSocket);
        WSACleanup();
        return 1;
    }
    printf("Socket bound correctly\n");

    // Listen to incoming connections
    if (listen(serverSocket, 3) == SOCKET_ERROR) {
        printf("Error: Socket listening failed\n");
        closesocket(serverSocket);
        WSACleanup();
        return 1;
    }
    printf("Listening for connections at port %d...\n", PORT);
    printf("Press 'q' for exit\n");

    // Setup unblocking socket
    u_long mode = 1;
    ioctlsocket(serverSocket, FIONBIO, &mode);

    // Server Main Loop
    while (running) {
        // Check if the key was pressed
        if (_kbhit()) {
            const int ch = _getch();
            if (ch == 'q' || ch == 'Q') {
                printf("\nExiting...\n");
                running = false;
                break;
            }
        }

        // Accept incoming connections
        clientSocket = accept(serverSocket, (struct sockaddr*)&clientAddr, &clientSize);
        if (clientSocket == INVALID_SOCKET) {
            if (WSAGetLastError() == WSAEWOULDBLOCK) {
                printf("Error: Connection accept failed\n");
            }
            Sleep(100);
            continue;
        }

        printf("New Connection accepted\n");

        // Receive data from a client
        const int bytesReceived = recv(clientSocket, buffer, BUFFER_SIZE, 0);
        if (bytesReceived > 0) {
            buffer[bytesReceived] = '\0';
            printf("Message received from client: %s\n", buffer);

            // Send data to a client
            const char* response = "Message received from server\n";
            send(clientSocket, response, (int)strlen(response), 0);
        }

        // Close connection
        closesocket(clientSocket);
    }

    // Cleaning
    closesocket(serverSocket);
    WSACleanup();
    return 0;
}
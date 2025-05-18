#include <stdio.h>
#include "SocketServer.h"

#define BUFFER_SIZE 1024


int main(void) {
    const SOCKET serverSocket = get_server_socket();
    if (serverSocket == INVALID_SOCKET) {
        return 1;
    }

    char buffer[BUFFER_SIZE];
    struct sockaddr_in clientAddr;
    int clientSize;

    while (1) {
        clientSize = sizeof(clientAddr);
        const SOCKET clientSocket = accept(serverSocket, (struct sockaddr *) &clientAddr, &clientSize);

        if (clientSocket == INVALID_SOCKET) {
            printf("Accept failed: %d\n", WSAGetLastError());
            continue;
        }
        printf("New client connected\n");

        int clientConnected = 1;
        while (clientConnected) {
            int bytesReceived = recv(clientSocket, buffer, BUFFER_SIZE - 1, 0);
            if (bytesReceived <= 0) {
                printf("Client disconnected or error.\n");
                break;
            }

            buffer[bytesReceived] = '\0';
            printf("Received: %s\n", buffer);

            const char* response;

            // Check for exit command
            if (strcmp(buffer, "exit\n") == 0) {
                clientConnected = 0;
                response = "Bye!\n";
            } else {
                response = "Hello, World!\n";
            }

            // Send response
            send(clientSocket, response, (int)strlen(response), 0);
            printf("Sent: %s\n", response);
        }

        closesocket(clientSocket);
    }

    close_server();
    return 0;
}
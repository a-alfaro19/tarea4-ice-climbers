#ifndef SOCKETSERVER_H
#define SOCKETSERVER_H

#include <winsock2.h>
#include "clientes.h"

/**
 * Enum to identify the client type
 */
typedef enum {
    PLAYER,
    OBSERVER
} ClientType;

/**
 * Structure to store the client information
 */
typedef struct {
    SOCKET socket;
    int id;
    ClientType type;
} ClientInfo;


typedef enum {
    SIN_PARTIDA,
    MODO_UNO_JUGADOR,
    MODO_DOS_JUGADORES
} ModoJuego;


/**
 * @brief Initialize Winsock and sets its initialized state as true.
 * @return 1 if Winsock was initialized successfully, 0 otherwise.
 */
int initialize_winsock();

/**
 * @brief Initializes and returns a singleton server socket.
 * @return A valid SOCKET if successful, or INVALID_SOCKET on error.
 */
SOCKET get_server_socket();

/**
 * @brief Closes the server socket and cleans up the Winsock environment.
 */
void close_server();

/**
 * @brief Receives a request from the client through the socket
 * @param socket The client socket from which to receive the request
 * @param buffer Buffer where the received message will be stored
 * @param buffer_size Maximum size of the buffer
 * @return 1 if reception was successful, 0 if there was an error
 */
int receive_request(SOCKET socket, char *buffer, int buffer_size);

/**
 * @brief Sends a response to the client through the socket.
 * @param socket The client socket to which the response will be sent
 * @param response Character string containing the response to be sent
 * @return 1 if sending was successful, 0 if there was an error
 */
int send_response(SOCKET socket, const char *response);

/**
 * @brief Handles communication with a connected client in a separate thread.
 * This function runs in a new thread for each connecting client.
 * It processes client identification (PLAYER/OBSERVER) and maintains
 * communication until the client disconnects.
 *
 * @param param Pointer to the client socket (must be freed within the function)
 * @return 0 when the client connection ends
 */
DWORD WINAPI handle_client(LPVOID param);

/**
 * @brief Loop de juego que actualiza y envía el estado a todos los clientes conectados.
 * Este loop se ejecuta constantemente en segundo plano, independientemente de las acciones del jugador.
 */
DWORD WINAPI game_loop(LPVOID param);

DWORD WINAPI consoleThread(LPVOID lpParameter);

#endif // SOCKETSERVER_H

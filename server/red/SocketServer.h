#ifndef SOCKETSERVER_H
#define SOCKETSERVER_H

#include <winsock2.h>

/**
 * @brief Initialize winsock and sets its initialized state as true.
 * @return Integer that represents if winsock was initialized.
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

#endif //SOCKETSERVER_H

#ifndef MENSAJES_H
#define MENSAJES_H

#include <winsock2.h>
#include <stdio.h>
#include "../juego/Map.h"


/**
 * Recibe una acción textual del cliente (como "MOVER:L").
 */
int recibir_accion(SOCKET socket_cliente, char* buffer, int tamanio);

// int enviar_juego(SOCKET socket, const Juego* juego);

void imprimir_bytes(const void* data, size_t size, const char* etiqueta);

/**
 * @brief Sends the Game map to the client
 * @param socket Socket server
 * @param map Game map to send
 * @return 1 if the map was sent correctly and 0 if not.
 */
int send_map(SOCKET socket, Tile map[TOTAL_ROWS][TILES_PER_ROW]);

#endif

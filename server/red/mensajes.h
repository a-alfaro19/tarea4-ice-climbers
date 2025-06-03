#ifndef MENSAJES_H
#define MENSAJES_H

#include <winsock2.h>
#include <stdio.h>
#include "../juego/Map.h"

// /**
//  * Envía un paquete de bloques al cliente.
//  */
// int enviar_bloques(SOCKET socket, const PaqueteBloques* paquete);

/**
 * Recibe una acción textual del cliente (como "MOVER:L").
 */
int recibir_accion(SOCKET socket_cliente, char* buffer, int tamanio);


int enviar_juego(SOCKET socket, const Juego* juego);

void imprimir_bytes(const void* data, size_t size, const char* etiqueta);

#endif

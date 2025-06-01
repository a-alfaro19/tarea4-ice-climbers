#ifndef MENSAJES_H
#define MENSAJES_H

#include "../util/tipos.h"
#include <winsock2.h>
#include <stdio.h>
#include <string.h>
/**
 * @brief Envía el estado completo del juego al cliente.
 * @param socket_cliente Socket válido del cliente.
 * @param juego Puntero a la estructura del juego.
 * @return Número de bytes enviados o -1 si hay error.
 */
int enviar_juego(SOCKET socket_cliente, const Juego *juego);

/**
 * @brief Recibe una acción o mensaje del cliente (por ejemplo: mover jugador).
 * @param socket_cliente Socket válido del cliente.
 * @param buffer Buffer donde guardar la acción recibida.
 * @param tamanio Tamaño del buffer.
 * @return Número de bytes recibidos o -1 si hay error.
 */
int recibir_accion(SOCKET socket_cliente, void *buffer, int tamanio);

#endif

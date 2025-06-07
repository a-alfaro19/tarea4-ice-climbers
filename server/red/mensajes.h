#ifndef MENSAJES_H
#define MENSAJES_H

#include <winsock2.h>
#include <stdio.h>
#include "../juego/mapa.h"

/**
 * Envía al cliente todos los bloques visibles en el mapa en forma plana.
 *
 * @param socket Socket del cliente al que se enviará.
 * @param paquete Paquete de bloques visibles a enviar.
 * @return Número de bytes enviados o -1 si ocurrió un error.
 */
int enviar_bloques(SOCKET socket, const PaqueteBloques* paquete);

/**
 * Recibe una acción textual desde el cliente (como "MOVER:L", "BRINCAR").
 *
 * @param socket_cliente Socket del cliente que envía.
 * @param buffer Buffer donde se almacenará la acción.
 * @param tamanio Tamaño máximo del buffer.
 * @return Número de bytes recibidos o -1 si hay error.
 */
int recibir_accion(SOCKET socket_cliente, char* buffer, int tamanio);

/**
 * Envía al cliente toda la información del juego actual:
 * jugadores, obstáculos, frutas, estado general y pterodáctilo.
 *
 * @param socket Socket del cliente.
 * @param juego Puntero al estado actual del juego.
 * @return 0 si todo fue exitoso, -1 si ocurrió un error.
 */
int enviar_juego(SOCKET socket, Juego* juego);

/**
 * Imprime los bytes de una estructura (para debug en desarrollo).
 * No se usa en producción.
 *
 * @param data Puntero a los datos.
 * @param size Tamaño en bytes de los datos.
 * @param etiqueta Etiqueta para identificar la impresión.
 */

void imprimir_bytes(const void* data, size_t size, const char* etiqueta);

#endif

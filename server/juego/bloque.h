#ifndef BLOQUE_H
#define BLOQUE_H

#include <winsock2.h>

/**
 * Estructura que representa un bloque destructible o interactivo en el juego.
 */
typedef struct {
    int id;
    int x;
    int y;
    int vida;
    int destruido;
} Bloque;

/**
 * Estructura para enviar múltiples bloques en un solo paquete.
 */
typedef struct {
    int cantidad;
    Bloque bloques[10]; // Puedes ajustar el tamaño si es necesario
} PaqueteBloques;

/**
 * Crea un nuevo bloque con los parámetros dados.
 */
Bloque crear_bloque(int id, int x, int y, int vida);

/**
 * Envía un paquete de bloques a un cliente por socket.
 */
int enviar_bloques(SOCKET socket_cliente, const PaqueteBloques *paquete);

#endif

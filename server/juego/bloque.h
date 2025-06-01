// bloque.h
#ifndef BLOQUE_H
#define BLOQUE_H
#include <winsock2.h>
#include "../util/tipos.h"

int enviar_bloques(SOCKET socket_cliente, Bloque *bloques, int cantidad);

#endif
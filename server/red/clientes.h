#ifndef CLIENTES_H
#define CLIENTES_H

#include <winsock2.h>
#include "../util/log.h"

typedef struct {
    SOCKET socket;
    int es_jugador;     // 1 = jugador, 0 = observador
    int id_jugador;     // 0 = Popo, 1 = Nana, -1 si
    int observando_a;
} ClienteConectado;

int registrar_cliente(SOCKET socket, int es_jugador, int id_jugador);
ClienteConectado* obtener_clientes();
int total_clientes();
void remover_cliente(SOCKET socket);
int contar_observadores_de(int id_jugador);
int get_player_clients();
int get_observer_clients();

#endif
#include "clientes.h"

#include <stdio.h>

// Estructuras internas
static ClienteConectado clientes[MAX_CLIENTS]; // Estructuras internas
static int num_clientes = 0;
static int player_clients = 0;
static int observer_clients = 0;

/**
 * Registra un nuevo cliente, validando si hay espacio disponible.
 * En el caso de observadores, también valida que no haya más de 2 por jugador.
 */
int registrar_cliente(SOCKET socket, int es_jugador, int id_jugador) {
    if (num_clientes >= MAX_CLIENTS) return 0;

    ClienteConectado nuevo;
    nuevo.socket = socket;
    nuevo.es_jugador = es_jugador;
    nuevo.id_jugador = id_jugador;

    if (!es_jugador) {
        if (contar_observadores_de(0) < 2) {
            nuevo.observando_a = 0;
        } else if (contar_observadores_de(1) < 2) {
            nuevo.observando_a = 1;
        } else {
            printf("Límite de observadores alcanzado. Cliente rechazado.");
            return 0;
        }
    } else {
        nuevo.observando_a = -1;
    }

    clientes[num_clientes++] = nuevo;
    return 1;
}
/**
 * Retorna el número total de jugadores conectados.
 */
int get_player_clients() {
    return player_clients;
}
/**
 * Retorna el número total de observadores conectados.
 */
int get_observer_clients() {
    return observer_clients;
}
/**
 * Retorna el número total de clientes conectados (jugadores + observadores).
 */
int total_clientes() {
    return num_clientes;
}
/**
 * Elimina un cliente de la lista según su socket.
 * Reorganiza el arreglo de clientes para mantenerlo compacto.
 */
void remover_cliente(SOCKET socket) {
    for (int i = 0; i < num_clientes; i++) {
        if (clientes[i].socket == socket) {
            printf("Eliminando cliente socket %d (observando_a = %d)\n", socket, clientes[i].observando_a); // DEBUG
            clientes[i] = clientes[num_clientes - 1];
            num_clientes--;
            break;
        }
    }
}
/**
 * Cuenta cuántos observadores están viendo al jugador con ID dado.
 */
int contar_observadores_de(int id_jugador) {
    int count = 0;
    for (int i = 0; i < num_clientes; i++) {
        if (!clientes[i].es_jugador && clientes[i].observando_a == id_jugador)
            count++;
    }
    return count;
}
/**
 * Retorna un puntero al arreglo global de clientes conectados.
 */
ClienteConectado* obtener_clientes() {
    return clientes;
}


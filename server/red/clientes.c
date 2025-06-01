#include "clientes.h"
#define MAX_CLIENTES 6

static ClienteConectado clientes[MAX_CLIENTES];
static int num_clientes = 0;

int registrar_cliente(SOCKET socket, int es_jugador, int id_jugador) {
    if (num_clientes >= MAX_CLIENTES) return 0;

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
            log_info("Límite de observadores alcanzado. Cliente rechazado.");
            return 0;
        }
    } else {
        nuevo.observando_a = -1;
    }

    clientes[num_clientes++] = nuevo;
    return 1;
}

int total_clientes() {
    return num_clientes;
}


void remover_cliente(SOCKET socket) {
    for (int i = 0; i < num_clientes; i++) {
        if (clientes[i].socket == socket) {
            // Reemplazar con el último cliente de la lista
            clientes[i] = clientes[num_clientes - 1];
            num_clientes--;
            break;
        }
    }
}
int contar_observadores_de(int id_jugador) {
    int count = 0;
    for (int i = 0; i < num_clientes; i++) {
        if (!clientes[i].es_jugador && clientes[i].observando_a == id_jugador)
            count++;
    }
    return count;
}

ClienteConectado* obtener_clientes() {
    return clientes;
}


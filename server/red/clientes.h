#ifndef CLIENTES_H
#define CLIENTES_H

#include <winsock2.h>
#include "../juego/constantes.h"

/**
 * Estructura que representa a un cliente conectado (jugador u observador).
 */
typedef struct {
    SOCKET socket;        // Socket del cliente conectado
    int es_jugador;       // 1 si es jugador, 0 si es observador
    int id_jugador;       // 0 = Popo, 1 = Nana, -1 si no aplica
    int observando_a;     // Para observadores: 0 = Popo, 1 = Nana, -1 si no aplica
} ClienteConectado;

/**
 * Registra un nuevo cliente en la estructura global.
 *
 * @param socket Socket del cliente.
 * @param es_jugador 1 si es jugador, 0 si es observador.
 * @param id_jugador Identificador del jugador (0 o 1), -1 si es observador.
 * @return 1 si se registró correctamente, 0 si no hay espacio o se excedió el límite.
 */
int registrar_cliente(SOCKET socket, int es_jugador, int id_jugador);

/**
 * Retorna un puntero al arreglo global de clientes conectados.
 */
ClienteConectado* obtener_clientes();

/**
 * Retorna el número total de clientes conectados.
 */
int total_clientes();

/**
 * Elimina un cliente específico.
 *
 * @param socket Socket del cliente a remover.
 */
void remover_cliente(SOCKET socket);
/**
 * Cuenta cuántos observadores están viendo al jugador con el id dado.
 *
 * @param id_jugador 0 = Popo, 1 = Nana.
 * @return Número de observadores viendo a ese jugador.
 */
int contar_observadores_de(int id_jugador);
/**
 * Retorna el número total de clientes jugador conectados.
 */
int get_player_clients();
/**
 * Retorna el número total de observadores conectados.
 */
int get_observer_clients();

#endif
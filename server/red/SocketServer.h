#ifndef SOCKETSERVER_H
#define SOCKETSERVER_H
#include <winsock2.h>
#include "clientes.h"

/**
 * Tipo de cliente conectado.
 */
typedef enum {
    PLAYER, // Cliente juagdor (popo o nana)
    OBSERVER // Cliente observador
} ClientType;

/**
 * Información de un cliente conectado a través del servidor.
 */
typedef struct {
    SOCKET socket; // Socket del cliente
    int id; // ID del jugador u observador
    ClientType type; // tipo de cliente (player u observer)
} ClientInfo;

/**
 * Estado actual del modo de juego.
 */
typedef enum {
    SIN_PARTIDA, // No hay partida activa
    MODO_UNO_JUGADOR, // Solo Popo está jugando
    MODO_DOS_JUGADORES // Popo y Nana están activos
} ModoJuego;


/**
 * Inicializa la librería Winsock. Solo se ejecuta una vez.
 *
 * @return 1 si se inicializó correctamente, 0 si hubo error.
 */
int initialize_winsock();

/**
 * Retorna el socket principal del servidor, escuchando por nuevas conexiones.
 * Se asegura de que solo se cree una instancia (patrón Singleton).
 *
 * @return Socket válido o INVALID_SOCKET si hubo error.
 */
SOCKET get_server_socket();

/**
 * Cierra el socket del servidor y limpia el entorno de Winsock.
 */
void close_server();

/**
 * Recibe una solicitud (texto) del cliente conectado.
 *
 * @param socket Socket del cliente.
 * @param buffer Buffer donde se almacena el mensaje recibido.
 * @param buffer_size Tamaño máximo del buffer.
 * @return 1 si se recibió correctamente, 0 si hubo error o desconexión.
 */
int receive_request(SOCKET socket, char *buffer, int buffer_size);

/**
 * Envía una cadena de respuesta al cliente.
 *
 * @param socket Socket del cliente.
 * @param response Texto a enviar.
 * @return 1 si se envió correctamente, 0 si hubo error.
 */
int send_response(SOCKET socket, const char *response);

/**
 * Función principal que maneja cada cliente conectado en un hilo separado.
 * Procesa identificación, acciones y desconexión.
 *
 * @param param Puntero al socket del cliente.
 * @return 0 cuando el cliente se desconecta.
 */
DWORD WINAPI handle_client(LPVOID param);

/**
 * Loop del juego que actualiza constantemente el estado y lo envía a los clientes.
 * Corre en un hilo independiente (20 FPS).
 */
DWORD WINAPI game_loop(LPVOID param);
/**
 * Hilo adicional que permite al usuario ingresar comandos desde consola para generar obstáculos.
 *
 * @param lpParameter Puntero al juego (Juego*).
 * @return 0 cuando el hilo termina.
 */
DWORD WINAPI consoleThread(LPVOID lpParameter);

#endif // SOCKETSERVER_H

#ifndef OBSERVER_H
#define OBSERVER_H

#include "../util/tipos.h"
#include <winsock2.h>

/**
 * Notifica a todos los clientes conectados (jugadores y observadores)
 * con el estado actualizado del juego.
 */
void notificar_observers(const Juego *juego);

#endif

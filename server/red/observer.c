#include "observer.h"
#include "clientes.h"
#include "mensajes.h"

void notificar_observers(const Juego *juego) {
    ClienteConectado *clientes = obtener_clientes();
    int total = total_clientes();

    for (int i = 0; i < total; i++) {
        enviar_juego(clientes[i].socket, juego);
    }
}

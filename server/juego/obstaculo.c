#include "obstaculo.h"
#include <string.h>

Obstaculo crear_obstaculo(const char *tipo, int x, int y, char direccion) {
    Obstaculo obs;
    strcpy(obs.tipo, tipo);
    obs.x = x;
    obs.y = y;
    obs.direccion = direccion;
    obs.activo = 1;
    return obs;
}

void mover_obstaculo(Obstaculo *obs) {
    if (obs->direccion == 'L') obs->x--;
    else if (obs->direccion == 'R') obs->x++;
    else if (obs->direccion == 'U') obs->y--;
    else if (obs->direccion == 'D') obs->y++;
}

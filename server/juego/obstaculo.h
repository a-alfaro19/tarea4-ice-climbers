#ifndef OBSTACULO_H
#define OBSTACULO_H

#include "../util/tipos.h"

Obstaculo crear_obstaculo(const char *tipo, int x, int y, char direccion);
void mover_obstaculo(Obstaculo *obs);

#endif

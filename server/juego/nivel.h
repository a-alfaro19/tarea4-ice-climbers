#ifndef NIVEL_H
#define NIVEL_H

#include "../util/tipos.h"
#include "obstaculo.h"
#include "fruta.h"

struct Nivel {
    int id;
    int num_obstaculos;
    int num_frutas;
    Obstaculo* obstaculos[10];
    Fruta frutas[4];
};

void inicializar_nivel(Nivel *nivel, int id);
void agregar_obstaculo_a_nivel(Nivel *nivel, Obstaculo *obs);
void agregar_fruta_a_nivel(Nivel *nivel, Fruta fruta);
int hay_piso_en_y(Nivel *nivel, int x, int y);

#endif

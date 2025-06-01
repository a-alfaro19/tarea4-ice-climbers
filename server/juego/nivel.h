#ifndef NIVEL_H
#define NIVEL_H

#include "../util/tipos.h"

void inicializar_nivel(Nivel *nivel, int id);
void agregar_obstaculo_a_nivel(Nivel *nivel, Obstaculo_st obs);
void agregar_fruta_a_nivel(Nivel *nivel, Fruta fruta);

#endif

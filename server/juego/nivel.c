#include "nivel.h"

void inicializar_nivel(Nivel *nivel, int id) {
    nivel->id = id;
    nivel->num_obstaculos = 0;
    nivel->num_frutas = 0;
}

void agregar_obstaculo_a_nivel(Nivel *nivel, Obstaculo obs) {
    if (nivel->num_obstaculos < 10) {
        nivel->obstaculos[nivel->num_obstaculos++] = obs;
    }
}

void agregar_fruta_a_nivel(Nivel *nivel, Fruta fruta) {
    if (nivel->num_frutas < 4) {
        nivel->frutas[nivel->num_frutas++] = fruta;
    }
}

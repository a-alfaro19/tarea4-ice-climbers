#include "nivel.h"

void inicializar_nivel(Nivel *nivel, int id) {
    nivel->id = id;
    nivel->num_obstaculos = 0;
    nivel->num_frutas = 0;
}

void agregar_obstaculo_a_nivel(Nivel *nivel, Obstaculo_st obs) {
    if (nivel->num_obstaculos < 10) {
        nivel->obstaculos[nivel->num_obstaculos++] = obs;
    }
}

void agregar_fruta_a_nivel(Nivel *nivel, Fruta fruta) {
    if (nivel->num_frutas < 4) {
        nivel->frutas[nivel->num_frutas++] = fruta;
    }
}

int hay_piso_en_y(Nivel *nivel, int x, int y) {
    // Si hay un obstáculo justo debajo, es piso
    for (int i = 0; i < nivel->num_obstaculos; i++) {
        if (nivel->obstaculos[i].activo && nivel->obstaculos[i].x == x && nivel->obstaculos[i].y == y)
            return 1;
    }
    return 0;
}

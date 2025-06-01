#include "jugador.h"


void mover_jugador(Jugador *jugador, char direccion) {
    jugador->direccion = direccion;
    if (direccion == 'L') jugador->x -= 1;
    if (direccion == 'R') jugador->x += 1;
}

void golpear(Jugador *jugador, Nivel *nivel) {
    printf("%s golpea con el martillo!\n", jugador->nombre);
    int target_x = jugador->x + (jugador->direccion == 'R' ? 1 : -1);

    for (int i = 0; i < nivel->num_obstaculos; i++) {
        Obstaculo *obs = nivel->obstaculos[i];  // ya es puntero

        if (obs->activo && obs->pos.x == target_x && obs->pos.y == jugador->y) {
            obs->activo = 0;

            if (obs->tipo == YETI)
                sumar_puntaje(jugador, PUNTOS_YETI);
            else if (obs->tipo == AVE)
                sumar_puntaje(jugador, PUNTOS_AVE);
        }
    }
}

void perder_vida(Jugador *jugador) {
    if (jugador->vidas > 0) jugador->vidas--;
}

void brincar_jugador(Jugador *jugador) {
    jugador->y -= 1;
}

void sumar_puntaje(Jugador *jugador, int puntos) {
    jugador->puntaje += puntos;
}

#include "juego.h"

void inicializar_juego(Juego *juego) {
    memset(juego, 0, sizeof(Juego));
    strcpy(juego->jugadores[0].nombre, "Popo");
    strcpy(juego->jugadores[1].nombre, "Nana");
    juego->jugadores[0].vidas = juego->jugadores[1].vidas = 3;
    juego->velocidad = 1;
}

void actualizar_juego(Juego *juego) {
    Nivel *nivel = &juego->niveles[juego->nivel_actual];

    // Mover obstáculos
    for (int i = 0; i < nivel->num_obstaculos; i++) {
        Obstaculo *obs = nivel->obstaculos[i];
        if (obs->activo && obs->mover != NULL)
            obs->mover(obs);
    }

    // Verificar colisiones con obstáculos
    for (int j = 0; j < 2; j++) {
        Jugador *jug = &juego->jugadores[j];
        for (int i = 0; i < nivel->num_obstaculos; i++) {
            Obstaculo *obs = nivel->obstaculos[i];
            if (obs->activo && obs->pos.x == jug->x && obs->pos.y == jug->y) {
                perder_vida(jug);
                obs->activo = 0;
                const char *tipo_str = (obs->tipo == YETI) ? "Yeti" :
                                       (obs->tipo == AVE) ? "Ave" :
                                       (obs->tipo == BLOQUE_HIELO) ? "Bloque de Hielo" : "Obstáculo";
                printf("%s fue golpeado por un %s\n", jug->nombre, tipo_str);
            }
        }
    }

    // Verificar si deben caer por falta de piso
    for (int j = 0; j < 2; j++) {
        Jugador *jug = &juego->jugadores[j];
        int abajo = jug->y + 1;
        if (!hay_piso_en_y(nivel, jug->x, abajo)) {
            perder_vida(jug);
            printf("%s cayó por falta de piso y perdió una vida\n", jug->nombre);
        }
    }

    // Sincronizar altura entre jugadores (si uno está muy abajo)
    int y0 = juego->jugadores[0].y;
    int y1 = juego->jugadores[1].y;
    if (abs(y0 - y1) > 2) {
        int abajo = (y0 > y1) ? 0 : 1;
        perder_vida(&juego->jugadores[abajo]);
        juego->jugadores[abajo].y = juego->jugadores[1 - abajo].y;
        printf("%s estaba muy abajo y perdió una vida. Reposicionado\n", juego->jugadores[abajo].nombre);
    }

    // Recolección de frutas si estamos en fase bonus
    if (juego->en_fase_bonus) {
        for (int j = 0; j < 2; j++) {
            Jugador *jug = &juego->jugadores[j];
            for (int i = 0; i < nivel->num_frutas; i++) {
                Fruta *f = &nivel->frutas[i];
                if (!f->recolectada && f->x == jug->x && f->y == jug->y) {
                    f->recolectada = 1;
                    sumar_puntaje(jug, f->valor);
                    printf("%s recolectó una %s (+%d pts)\n", jug->nombre, f->tipo, f->valor);
                }
            }
        }
    }

    // Avanzar de nivel si ambos jugadores llegaron arriba (e.g. y <= 1)
    if (!juego->en_fase_bonus && juego->nivel_actual < MAX_NIVELES - 1) {
        if (juego->jugadores[0].y <= 1 && juego->jugadores[1].y <= 1) {
            juego->nivel_actual++;
            printf("Avanzando al nivel %d\n", juego->nivel_actual);
        }
    }

    // Entrar en fase bonus si están en último nivel
    if (juego->nivel_actual == MAX_NIVELES - 1 && !juego->en_fase_bonus) {
        juego->en_fase_bonus = 1;
        for (int i = 0; i < 2; i++) juego->jugadores[i].vidas++;
        reiniciar_juego(juego);
        printf("Entrando en fase BONUS. +1 vida y reinicio del juego.\n");
    }
}

void reiniciar_juego(Juego *juego) {
    juego->nivel_actual = 0;
    juego->en_fase_bonus = 0;
    juego->velocidad += 1;
}

void imprimir_estado_juego(Juego *juego) {
    printf("Nivel: %d | Velocidad: %d | Bonus: %d\n",
           juego->nivel_actual, juego->velocidad, juego->en_fase_bonus);
}
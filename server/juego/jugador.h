#ifndef JUGADOR_H
#define JUGADOR_H
/**
 * Representa un jugador del juego (Popo o Nana).
 * Almacena toda la información necesaria para su estado y comportamiento.
 */
typedef struct Jugador {
    char nombre[10];     // Nombre del jugador (ej: "Popo", "Nana")
    float y_real;        // Posición vertical precisa (para salto/gravedad)
    int x, y;            // Posición entera en el mapa (columna y fila)
    int vidas;           // Cantidad de vidas restantes
    int puntaje;         // Puntaje acumulado
    char direccion;      // Dirección actual: 'L' = izquierda, 'R' = derecha
    float vy;            // Velocidad vertical (para salto/gravedad)
    int en_el_aire;      // 1 si está en el aire, 0 si está sobre un

    int puntos_hielo;
    int puntos_ave;
    int puntos_yeti;
    int puntos_naranja;
    int puntos_banano;
    int puntos_berenjena;
    int puntos_lechuga;
} Jugador;
/**
 * Resta una vida al jugador (si tiene) y muestra mensaje en consola.
 *
 * @param j Puntero al jugador afectado.
 */
void perder_vida(Jugador* j);
/**
 * Suma puntos al jugador y muestra mensaje con puntaje total.
 *
 * @param j Puntero al jugador.
 * @param valor Cantidad de puntos que se agregan al puntaje.
 */
void sumar_puntaje(Jugador* j, int valor);

#endif



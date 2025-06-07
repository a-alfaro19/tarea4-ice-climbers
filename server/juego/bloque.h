#ifndef BLOQUE_H
#define BLOQUE_H
/**
 * Representa un bloque en el mapa del juego.
 * Puede ser parte de una lista enlazada dentro de un nivel.
 */
typedef struct Bloque {
    int x, y;           // coordenadas
    int tipo;           // 1 = destructible, 2 = indestructible
    int activo;         // 1 = visible, 0 = destruido
    struct Bloque* siguiente; // Siguiente bloque en la lista
} Bloque;
/**
 * Versión plana del bloque (sin puntero siguiente), usada para enviar por red.
 */
typedef struct {
    int x, y;
    int tipo;
    int activo;
} BloquePlano;
/**
 * Crea un nuevo bloque en memoria dinámica.
 */
Bloque* crear_bloque(int x, int y, int tipo);
/**
 * Libera toda la memoria usada por la lista de bloques.
 */
void destruir_bloques(Bloque* cabeza);
/**
 * Imprime todos los bloques de una lista (para debug).
 */
void imprimir_bloques(Bloque* cabeza);

#endif

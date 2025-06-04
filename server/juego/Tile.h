#ifndef BLOQUE_H
#define BLOQUE_H

typedef enum {
    NORMAL,
    FLAT,
    FIXED_TILE,
    EMPTY
} TileType;

typedef struct {
    int x, y; // Tile position
    TileType type;
} Tile;

#endif

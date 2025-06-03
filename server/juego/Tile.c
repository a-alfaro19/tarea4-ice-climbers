#include <stdlib.h>
#include "Tile.h"

Tile* create_tile(const int x, const int y, const TileType type) {
    Tile* tile = malloc(sizeof(Tile));
    tile->x = x;
    tile->y = y;
    tile->type = type;
    return tile;
}

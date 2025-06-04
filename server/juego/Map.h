#ifndef MAPA_H
#define MAPA_H

#include "Tile.h"

#define MAX_BLOQUES 512  // máximo a enviar

// Tiles per Row
#define FIXED_TILES_PER_SIDE 4
#define TILES_BETWEEN 20
#define TILES_PER_ROW (FIXED_TILES_PER_SIDE + TILES_BETWEEN + FIXED_TILES_PER_SIDE) // Map Cols

// Floor Constants
#define FLOORS_PER_LEVEL 8
#define FLOOR_HEIGHT 1
#define PRE_FLOOR_HEIGHT 1
#define TOTAL_FLOOR_HEIGHT (FLOOR_HEIGHT + PRE_FLOOR_HEIGHT)

// Player Free Space
#define ROWS_BETWEEN_FLOORS 4

#define TOTAL_ROWS_FOR_FLOORS  ((FLOORS_PER_LEVEL - 1) * TOTAL_FLOOR_HEIGHT)
#define TOTAL_ROWS_BETWEEN_FLOORS ((FLOORS_PER_LEVEL - 1) * ROWS_BETWEEN_FLOORS)
#define TOTAL_ROWS (1 + TOTAL_ROWS_FOR_FLOORS + TOTAL_ROWS_BETWEEN_FLOORS) // Total Map Rows


extern Tile map[TOTAL_ROWS][TILES_PER_ROW];

/**
 * @brief Creates a random map
 */
void initialize_map();

/**
 * @brief Transform a TileType to its equivalent in String
 * @param type Type of the tile
 * @return A String representation of the Tile Type
 */
const char* tile_type_to_char(TileType type);

/**
 * @brief Prints the Game Map on console
 */
void print_map();

#endif

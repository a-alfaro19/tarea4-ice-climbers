#include <stdlib.h>
#include <stdio.h>
#include "Map.h"

Tile map[TOTAL_ROWS][TILES_PER_ROW];

void initialize_map() {
    // Set all Tiles as empty
    for (int i = 0; i < TOTAL_ROWS; i++) {
        for (int j = 0; j < TILES_PER_ROW; j++) {
            map[i][j].type = EMPTY;
        }
    }

    // Create Floors
    for (int floor = 0; floor < FLOORS_PER_LEVEL; floor++) {
        const int rowIndex = floor * (ROWS_BETWEEN_FLOORS + TOTAL_FLOOR_HEIGHT);
        for (int col = 0; col < TILES_PER_ROW; col++) {
            // Place Fixed Tiles
            if ((0 <= col && col <= 3) || (TILES_PER_ROW-1)-4 <= col) {
                map[rowIndex][col].type = FIXED_TILE;
            // Place Normal Tiles
            } else {
                map[rowIndex][col].type = NORMAL;
            }
            map[rowIndex][col].x = col;
            map[rowIndex][col].y = rowIndex;
        }
    }

    // Create Pre-Floors
    const TileType validTypes[] = { EMPTY, NORMAL, FLAT };
    const int numValidTypes = sizeof(validTypes) / sizeof(TileType);

    for (int floor = 0; floor < FLOORS_PER_LEVEL; floor++) {
        const int rowIndex = floor * (ROWS_BETWEEN_FLOORS + TOTAL_FLOOR_HEIGHT) - 1;

        // Skip the First Floor
        if (rowIndex == -1) continue;

        for (int col = 0; col < TILES_PER_ROW; col++) {
            // Place Empty Tiles
            if ((0 <= col && col <= 3) || (TILES_PER_ROW-1)-4 <= col) {
                map[rowIndex][col].type = EMPTY;
            // Place Any Tile
            } else {
                const TileType randomType = validTypes[rand() % numValidTypes];
                map[rowIndex][col].type = randomType;
            }
            map[rowIndex][col].x = col;
            map[rowIndex][col].y = rowIndex;
        }
    }
}

const char* tile_type_to_char(const TileType type) {
    switch (type) {
        case EMPTY:  return " ";
        case NORMAL: return "N";
        case FLAT:   return "F";
        case FIXED_TILE:  return "X";
        default:     return "?";
    }
}

void print_map() {
    printf("\n--- MAPA ---\n");
    for (int i = 0; i < TOTAL_ROWS; i++) {
        printf("Fila %2d | ", i);
        for (int j = 0; j < TILES_PER_ROW; j++) {
            printf("%s", tile_type_to_char(map[i][j].type));
        }
        printf("\n");
    }
}

//TODO Pasar el mapa al cliente y mostrarlo en la intefaz
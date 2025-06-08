package model;

/**
 * Enumeración que representa los tipos de bloques (tiles) posibles en el juego iCE Climber.
 */
public enum TileType {

    /** Espacio vacío (sin bloque). */
    EMPTY,

    /** Bloque normal, puede ser destruido por el jugador. */
    NORMAL,

    /** Superficie plana  */
    FLAT,

    /** Bloque fijo, no puede ser destruido. */
    FIXED_TILE;

    /**
     * Convierte un entero recibido desde el servidor al tipo de tile correspondiente.
     *
     * @param i valor entero que representa el tipo de tile (1 = NORMAL, 2 = FIXED_TILE)
     * @return tipo de tile correspondiente, o {@code EMPTY} por defecto
     */
    public static TileType fromInt(int i) {
        return switch (i) {
            case 1 -> NORMAL;
            case 2 -> FIXED_TILE;
            default -> EMPTY;
        };
    }
}


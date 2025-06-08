package model;

/**
 * Representa un bloque individual (tile) dentro del mapa del juego.
 *
 * <p>Cada tile tiene una posición (x, y) y un tipo {@link TileType} que define
 * si es vacío, normal, fijo u otro.</p>
 */
public class Tile {

    /** Coordenada horizontal del tile (columna). */
    public Integer x;

    /** Coordenada vertical del tile (fila). */
    public Integer y;

    /** Tipo del tile (por ejemplo, EMPTY, NORMAL, FIXED_TILE). */
    public TileType type;

    /**
     * Constructor que inicializa un nuevo tile con posición y tipo dado.
     *
     * @param x coordenada x (columna)
     * @param y coordenada y (fila)
     * @param type tipo del tile (ver {@link TileType})
     */
    public Tile(int x, int y, TileType type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }
}

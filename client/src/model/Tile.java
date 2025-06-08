package model;

public class Tile {
    public Integer x, y;
    public TileType type;

    public Tile(int x, int y, TileType type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }
}

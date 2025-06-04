package ui;

public enum TileType {
    EMPTY,
    NORMAL,
    FLAT,
    FIXED_TILE;

    public static TileType fromInt(int i) {
        return switch (i) {
            case 1 -> NORMAL;
            case 2 -> FIXED_TILE;
            default -> EMPTY;
        };
    }
}

package model;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;

public class Obstacle {
    protected int x, y; // Obstacle position
    protected BufferedImage image; // Obstacle image

    /**
     * Obstacle Constructor.
     * @param type Obstacle Type.
     * @param x Obstacle origin x position.
     * @param y Obstacle origin y position.
     */
    public Obstacle(ObstacleType type, int x, int y) throws IOException {
        // Set image
        String filePath = switch (type) {
            case YETI -> "src/ui/figuras/yeti.png";
            case BIRD -> "src/ui/figuras/aveA.png";
            case ICE_BLOCK -> "src/ui/figuras/hielo.png";
        };

        try {
            image = ImageIO.read(new File(filePath));
        } catch (IOException e) {
            System.err.println("Error loading Obstacle Image: " + e.getMessage());
        }

        this.x = x;
        this.y = y;
    }

    /**
     * Gets Obstacle x position.
     * @return Obstacle x position.
     */
    public int getX() {
        return this.x;
    }

    /**
     * Gets Obstacle y position.
     * @return Obstacle y position.
     */
    public int getY() {
        return this.y;
    }

    /**
     * Gets the obstacle image.
     * @return The obstacle image.
     */
    public BufferedImage getImage() {
        return this.image;
    }

    public static Obstacle readFrom(DataInputStream in) throws IOException {
        // Read type
        int type = readIntLE(in);
        int x = readIntLE(in);
        int y = readIntLE(in);

        return new Obstacle(ObstacleType.values()[type], x, y);
    }

    private static Integer readIntLE(DataInputStream in) throws IOException {
        int b1 = in.readUnsignedByte();
        int b2 = in.readUnsignedByte();
        int b3 = in.readUnsignedByte();
        int b4 = in.readUnsignedByte();
        return (b4 << 24) | (b3 << 16) | (b2 << 8) | b1;
    }
}

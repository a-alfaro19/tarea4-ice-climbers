package model;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
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
            case YETI -> "yeti.png";
            case BIRD -> "bird.png";
            case ICE_BLOCK -> "ice_block.png";
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
}

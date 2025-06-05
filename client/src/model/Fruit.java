package model;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Fruit {
    private int x, y; // Fruit position
    private final int score; // Fruit points
    private BufferedImage image; // Fruit image

    public Fruit(FruitType type, int x, int y) throws IOException {
        // Set score and image file
        String filePath = switch (type) {
            case ORANGE -> {
                score = 100;
                yield "src/ui/figuras/orange.png";
            }
            case BANANA -> {
                score = 200;
                yield "src/ui/figuras/banana.png";
            }
            case EGG_PLANT -> {
                score = 300;
                yield "src/ui/figuras/egg_plant.png";
            }
            case LETTUCE -> {
                score = 400;
                yield "src/ui/figuras/lettuce.png";
            }
        };

        // Set image
        try {
            image = ImageIO.read(new File(filePath));
        } catch (IOException e) {
            System.err.println("Error reading image: " + e.getMessage());
        }
    }

    /**
     * Gets the Fruit x position.
     * @return The Fruit x position.
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the Fruit y position.
     * @return The Fruit y position.
     */
    public int getY() {
        return y;
    }

    /**
     * Gets the Fruit score.
     * @return The Fruit score.
     */
    public int getScore() {
        return score;
    }

    /**
     * Gets the Fruit image.
     * @return The Fruit image.
     */
    public BufferedImage getImage() {
        return image;
    }
}

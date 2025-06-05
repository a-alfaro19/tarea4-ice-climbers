package model;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Yeti extends Obstacle {
    Direction originDir; // Yeti Origin Direction

    /**
     * Yeti Constructor
     * @param x The Yeti origin x position.
     * @param y The Yeti origin y position.
     * @throws IOException If the Yeti image was not read.
     */
    public Yeti(int x, int y) throws IOException {
        // Set base variables
        super(x, y);

        // Set Yeti image
        try {
            this.image = ImageIO.read(new File("filepath"));
        } catch (IOException e) {
            System.err.println("Error loading Yeti Image: " + e.getMessage());
        }


        // Set Yeti Origin Direction
        if (x == 0) {
            this.originDir = Direction.LEFT;
        } else {
            this.originDir = Direction.RIGHT;
        }
    }

    /**
     * Moves the Yeti one position, following his origin direction.
     */
    @Override
    public void move() {
        if (this.originDir == Direction.LEFT) {
            this.x += 1;
        } else {
            this.x -= 1;
        }
    }
}

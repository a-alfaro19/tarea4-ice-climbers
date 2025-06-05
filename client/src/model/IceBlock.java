package model;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class IceBlock extends Obstacle {
    /**
     * Ice Block Constructor
     * @param x The Ice Block origin x position.
     * @param y The Ice Block origin y position.
     * @throws IOException If the Ice Block image was not read.
     */
    public IceBlock(int x, int y) throws IOException {
        super(x, y);

        // Set Image
        try {
            this.image = ImageIO.read(new File("filepath"));
        } catch (IOException e) {
            System.err.println("Error loading Yeti Image: " + e.getMessage());
        }
    }

    /**
     * Moves the Ice Block downward.
     */
    @Override
    public void move() {
        // Always moves down
        this.y -= 1;
    }
}

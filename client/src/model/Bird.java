package model;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Bird extends Obstacle {
    /**
     * Bird Constructor
     * @param x Bird origin x position.
     * @param y Bird origin y position.
     * @throws IOException IOException If the Bird image was not read.
     */
    public Bird(int x, int y) throws IOException {
        // Set base variables
        super(x, y);

        // Set Bird image
        try {
            this.image = ImageIO.read(new File("filepath"));
        } catch (IOException e) {
            System.err.println("Error loading Yeti Image: " + e.getMessage());
        }
    }

    /**
     * Moves the Bird to the right shifting randomly his y position.
     */
    @Override
    public void move() {
        Direction[] validDirections = {Direction.UP, Direction.DOWN};
        Random rand = new Random();

        // Move Y Randomly
        Direction randomDir = validDirections[rand.nextInt(validDirections.length)];
        y = randomDir == Direction.UP ? y-1 : y+1;

        // Always Move Right
        x += 1;
    }
}

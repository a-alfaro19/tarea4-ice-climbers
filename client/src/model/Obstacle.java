package model;

import java.awt.image.BufferedImage;

public abstract class Obstacle {
    protected int lives; // Obstacle lives
    protected int x, y; // Obstacle position
    protected BufferedImage image; // Obstacle image

    /**
     * Obstacle Constructor.
     * @param x Obstacle origin x position.
     * @param y Obstacle origin y position.
     */
    public Obstacle(int x, int y) {
        this.x = x;
        this.y = y;
        this.lives = 1;
    }

    /**
     * Gets Obstacle lives.
     * @return Obstacle lives.
     */
    public int getLives() {
        return this.lives;
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

    /**
     * Makes the obstacle to lose one live.
     */
    public void loseLive() {
        this.lives -= 1;
    }

    /**
     * Sets the Obstacle x position.
     * @param x The new x position.
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Sets the Obstacle y position,
     * @param y The new y position.
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Makes the obstacle to move one position.
     */
    public abstract void move();
}

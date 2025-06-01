package ui;

import model.Jugador;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.IOException;
import javax.imageio.ImageIO;

public class GamePanel extends JPanel {
    private final String miNombre;
    private final BufferedWriter output;
    private BufferedImage popoImg;
    private BufferedImage nanaImg;
    private Jugador[] jugadores;
    private final int TILE_SIZE = 50;  // Puedes ajustar este valor según tu escala

    public GamePanel(String miNombre, BufferedWriter output) {
        this.miNombre = miNombre;
        this.output = output;
        setBackground(Color.BLACK);
        setFocusable(true);
        requestFocusInWindow();

        try {
            popoImg = ImageIO.read(getClass().getResource("/ui/figuras/popo.png"));
            nanaImg = ImageIO.read(getClass().getResource("/ui/figuras/nana.png"));
        } catch (IOException e) {
            System.err.println("No se pudieron cargar imágenes de los personajes.");
        }

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                try {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_LEFT -> output.write("MOVER:L\n");
                        case KeyEvent.VK_RIGHT -> output.write("MOVER:R\n");
                        case KeyEvent.VK_UP -> output.write("BRINCAR\n");
                        case KeyEvent.VK_SPACE -> output.write("GOLPEAR\n");
                    }
                    output.flush();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public void setJugadores(Jugador[] jugadores) {
        this.jugadores = jugadores;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (jugadores == null) return;

        for (Jugador j : jugadores) {
            int drawX = j.x * TILE_SIZE;
            int drawY = j.y * TILE_SIZE;

            if ("Popo".equalsIgnoreCase(j.nombre) && popoImg != null) {
                g.drawImage(popoImg, drawX, drawY, TILE_SIZE, TILE_SIZE, this);
            } else if ("Nana".equalsIgnoreCase(j.nombre) && nanaImg != null) {
                g.drawImage(nanaImg, drawX, drawY, TILE_SIZE, TILE_SIZE, this);
            }
        }
    }
}

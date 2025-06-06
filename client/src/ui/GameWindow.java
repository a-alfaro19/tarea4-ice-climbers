package ui;
import model.Bloque;
import model.Juego;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import javax.imageio.ImageIO;
import java.io.IOException;

public class GameWindow extends JFrame {
    private String nombreJugador = "";
    private final GamePanel gamePanel;

    public GameWindow(String nombreJugador, BufferedWriter output, boolean dosJugadores) {
        this.nombreJugador = nombreJugador;
        setTitle("iCE Climber - Cliente Jugador");
        setSize(1250, 660);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Panel central del juego
        gamePanel = new GamePanel(nombreJugador, output, dosJugadores, true);
        mainPanel.add(gamePanel, BorderLayout.CENTER);

        setContentPane(mainPanel);
        setVisible(true);

    }
    public void updateGame(Juego juego) {
        gamePanel.setJugadores(juego.jugadores);
        gamePanel.setNivelActual(juego.nivelActual);
        gamePanel.repaint();
    }

    public void updateBloques(java.util.List<Bloque> bloques) {
        gamePanel.setBloques(bloques, 30, 91);
        gamePanel.repaint();
    }


    // Clase para mostrar imagen y texto en horizontal
    class CharacterPanel extends JPanel {
        private BufferedImage image;
        private String label;

        public CharacterPanel(String resourcePath, String label) {
            this.label = label;
            setBackground(Color.DARK_GRAY);
            setLayout(new FlowLayout(FlowLayout.LEFT, 10, 20));

            try {
                image = ImageIO.read(getClass().getResource(resourcePath));
            } catch (IOException | IllegalArgumentException e) {
                System.err.println("No se pudo cargar la imagen: " + resourcePath);
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(200, 100); // ancho personalizado
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);

            // Dibujar imagen
            if (image != null) {
                g.drawImage(image, 10, 0, 45, 45, this);
            }

            // Dibujar texto
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 18));
            g.drawString(label, 90, 28);
        }
    }
}

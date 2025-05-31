package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;

public class GameWindow extends JFrame {

    public GameWindow() {
        setTitle("iCE Climber - Cliente Jugador");
        setSize(1300, 760);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());

        // Panel central del juego
        JPanel gamePanel = new JPanel();
        gamePanel.setBackground(Color.BLACK);
        mainPanel.add(gamePanel, BorderLayout.CENTER);

        // Panel inferior (abajo)
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(Color.DARK_GRAY);
        bottomPanel.setPreferredSize(new Dimension(1300, 40));

        // Paneles individuales alineados a izquierda y derecha
        JPanel popoPanel = new CharacterPanel("C:/Users/Brene/OneDrive/Escritorio/tarea4-ice-climbers/client/src/ui/figuras/popo.png", "VIDAS: ");
        JPanel nanaPanel = new CharacterPanel("C:\\Users\\Brene\\OneDrive\\Escritorio\\tarea4-ice-climbers\\client\\src\\ui\\figuras\\nana.png", "VIDAS: ");

        bottomPanel.add(popoPanel, BorderLayout.WEST);
        bottomPanel.add(nanaPanel, BorderLayout.EAST);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
        setVisible(true);
    }

    // Clase para mostrar imagen y texto en horizontal
    class CharacterPanel extends JPanel {
        private BufferedImage image;
        private String label;

        public CharacterPanel(String imagePath, String label) {
            this.label = label;
            setBackground(Color.DARK_GRAY);
            setLayout(new FlowLayout(FlowLayout.LEFT, 10, 20));

            try {
                image = ImageIO.read(new File(imagePath));
            } catch (IOException e) {
                System.err.println("No se pudo cargar la imagen: " + imagePath);
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
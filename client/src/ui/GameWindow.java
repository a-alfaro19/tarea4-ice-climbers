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
        setSize(1350, 760);
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
        bottomPanel.setPreferredSize(new Dimension(1350, 40));

        // Paneles individuales alineados a izquierda y derecha
        JPanel popoPanel = new CharacterPanel("/ui/figuras/popo.png", "VIDAS: ");
        JPanel nanaPanel = new CharacterPanel("/ui/figuras/nana.png", "VIDAS: ");

        bottomPanel.add(popoPanel, BorderLayout.WEST);
        bottomPanel.add(nanaPanel, BorderLayout.EAST);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
        setVisible(true);

        // Panel derecho para los números
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(Color.BLACK);
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setPreferredSize(new Dimension(100, 0)); // ancho del panel derecho

        // Panel izquierdo para futuras funcionalidades
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(Color.GRAY); // Color provisional
        leftPanel.setPreferredSize(new Dimension(200, 0)); // Ocupa todo el alto, 150 px de ancho
        mainPanel.add(leftPanel, BorderLayout.WEST);

        try {
            BufferedImage img1 = ImageIO.read(getClass().getResource("/ui/figuras/uno.png"));
            BufferedImage img2 = ImageIO.read(getClass().getResource("/ui/figuras/dos.png"));
            BufferedImage img3 = ImageIO.read(getClass().getResource("/ui/figuras/tres.png"));
            BufferedImage img4 = ImageIO.read(getClass().getResource("/ui/figuras/cuatro.png"));

            ImageIcon icon1 = new ImageIcon(img1.getScaledInstance(80, 80, Image.SCALE_SMOOTH));
            ImageIcon icon2 = new ImageIcon(img2.getScaledInstance(80, 80, Image.SCALE_SMOOTH));
            ImageIcon icon3 = new ImageIcon(img3.getScaledInstance(80, 80, Image.SCALE_SMOOTH));
            ImageIcon icon4 = new ImageIcon(img4.getScaledInstance(80, 80, Image.SCALE_SMOOTH));

            JLabel lbl1 = new JLabel(icon1);
            JLabel lbl2 = new JLabel(icon2);
            JLabel lbl3 = new JLabel(icon3);
            JLabel lbl4 = new JLabel(icon4);

            lbl1.setAlignmentX(Component.CENTER_ALIGNMENT);
            lbl2.setAlignmentX(Component.CENTER_ALIGNMENT);
            lbl3.setAlignmentX(Component.CENTER_ALIGNMENT);
            lbl4.setAlignmentX(Component.CENTER_ALIGNMENT);

            rightPanel.add(Box.createVerticalStrut(20));
            rightPanel.add(lbl4);
            rightPanel.add(Box.createVerticalStrut(110));
            rightPanel.add(lbl3);
            rightPanel.add(Box.createVerticalStrut(110));
            rightPanel.add(lbl2);
            rightPanel.add(Box.createVerticalStrut(110));
            rightPanel.add(lbl1);

        } catch (IOException | IllegalArgumentException e) {
            System.err.println("No se pudieron cargar las imágenes.");
        }

        mainPanel.add(rightPanel, BorderLayout.EAST);
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
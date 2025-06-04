package ui;
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

    public GameWindow(String nombreJugador, BufferedWriter output) {
        this.nombreJugador = nombreJugador;
        setTitle("iCE Climber - Cliente Jugador");
        setSize(1350, 760);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Panel central del juego
        // Panel central del juego
        gamePanel = new GamePanel(nombreJugador, output);
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


        // Panel izquierdo para funcionalidades avanzadas
        JPanel leftPanel = new JPanel(null);  // Layout nulo para posicionar libremente
        leftPanel.setBackground(Color.BLACK);
        leftPanel.setPreferredSize(new Dimension(200, 0)); // ancho fijo

        // Añadir espacio para empujar contenido hacia abajo
        leftPanel.add(Box.createVerticalGlue());

        try {
            BufferedImage marco = ImageIO.read(getClass().getResource("/ui/figuras/marco.png"));
            BufferedImage aveV = ImageIO.read(getClass().getResource("/ui/figuras/aveN.png"));
            BufferedImage hieloV = ImageIO.read(getClass().getResource("/ui/figuras/hielo.png"));
            BufferedImage yetiV = ImageIO.read(getClass().getResource("/ui/figuras/yeti.png"));
            BufferedImage popoV = ImageIO.read(getClass().getResource("/ui/figuras/popo.png"));
            BufferedImage nanaV = ImageIO.read(getClass().getResource("/ui/figuras/nana.png"));

            JLabel marco1Label = new JLabel(new ImageIcon(marco.getScaledInstance(200, 340, Image.SCALE_SMOOTH)));
            JLabel marco2Label = new JLabel(new ImageIcon(marco.getScaledInstance(200, 340, Image.SCALE_SMOOTH)));
            marco1Label.setBounds(0, 340, 200, 340); // marco en y=300
            marco2Label.setBounds(0,0,200,340); // marco en y=0
            JLabel itemLabel = new JLabel(new ImageIcon(hieloV.getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
            itemLabel.setBounds(20, 70, 50, 50); // imagen sobre el marco
            JLabel itemLabel2 = new JLabel(new ImageIcon(hieloV.getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
            itemLabel2.setBounds(20, 410, 50, 50); // imagen sobre el marco
            JLabel itemLabel3 = new JLabel(new ImageIcon(aveV.getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
            itemLabel3.setBounds(20, 120, 50, 50); // imagen sobre el marco
            JLabel itemLabel4 = new JLabel(new ImageIcon(aveV.getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
            itemLabel4.setBounds(20, 460, 50, 50); // imagen sobre el marco
            JLabel itemLabel5 = new JLabel(new ImageIcon(yetiV.getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
            itemLabel5.setBounds(20, 170, 50, 50); // imagen sobre el marco
            JLabel itemLabel6 = new JLabel(new ImageIcon(yetiV.getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
            itemLabel6.setBounds(20, 510, 50, 50); // imagen sobre el marco
            JLabel itemLabel7 = new JLabel(new ImageIcon(popoV.getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
            itemLabel7.setBounds(20, 270, 50, 50); // imagen sobre el marco
            JLabel itemLabel8 = new JLabel(new ImageIcon(nanaV.getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
            itemLabel8.setBounds(20, 610, 50, 50); // imagen sobre el marco

            JLabel textLabel = new JLabel("JUGADOR 1");
            textLabel.setForeground(Color.WHITE);
            textLabel.setFont(new Font("Monospaced", Font.BOLD, 20));
            textLabel.setBounds(47, 40, 200, 20); // texto a la par
            JLabel textLabel2 = new JLabel("JUGADOR 2");
            textLabel2.setForeground(Color.WHITE);
            textLabel2.setFont(new Font("Monospaced", Font.BOLD, 20));
            textLabel2.setBounds(47, 380, 200, 20); // texto a la par
            JLabel textLabel3 = new JLabel("10 X 0");
            textLabel3.setForeground(Color.WHITE);
            textLabel3.setFont(new Font("Monospaced", Font.BOLD, 20));
            textLabel3.setBounds(80, 70, 200, 20); // texto a la par
            JLabel textLabel4 = new JLabel("10 X 0");
            textLabel4.setForeground(Color.WHITE);
            textLabel4.setFont(new Font("Monospaced", Font.BOLD, 20));
            textLabel4.setBounds(80, 410, 200, 20); // texto a la par
            JLabel textLabel5 = new JLabel("800 X 0");
            textLabel5.setForeground(Color.WHITE);
            textLabel5.setFont(new Font("Monospaced", Font.BOLD, 20));
            textLabel5.setBounds(80, 120, 200, 20); // texto a la par
            JLabel textLabel6 = new JLabel("800 X 0");
            textLabel6.setForeground(Color.WHITE);
            textLabel6.setFont(new Font("Monospaced", Font.BOLD, 20));
            textLabel6.setBounds(80, 460, 200, 20); // texto a la par
            JLabel textLabel7 = new JLabel("400 X 0");
            textLabel7.setForeground(Color.WHITE);
            textLabel7.setFont(new Font("Monospaced", Font.BOLD, 20));
            textLabel7.setBounds(80, 170, 200, 20); // texto a la par
            JLabel textLabel8 = new JLabel("400 X 0");
            textLabel8.setForeground(Color.WHITE);
            textLabel8.setFont(new Font("Monospaced", Font.BOLD, 20));
            textLabel8.setBounds(80, 510, 200, 20); // texto a la par
            JLabel textLabel9 = new JLabel("TOTAL: ");
            textLabel9.setForeground(Color.WHITE);
            textLabel9.setFont(new Font("Monospaced", Font.BOLD, 20));
            textLabel9.setBounds(40, 220, 200, 20); // texto a la par
            JLabel textLabel10 = new JLabel("TOTAL: ");
            textLabel10.setForeground(Color.WHITE);
            textLabel10.setFont(new Font("Monospaced", Font.BOLD, 20));
            textLabel10.setBounds(40, 560, 200, 20); // texto a la par
            JLabel textLabel11 = new JLabel("BONUS: NO! ");
            textLabel11.setForeground(Color.WHITE);
            textLabel11.setFont(new Font("Monospaced", Font.BOLD, 18));
            textLabel11.setBounds(70, 270, 200, 20); // texto a la par
            JLabel textLabel12 = new JLabel("BONUS: NO!");
            textLabel12.setForeground(Color.WHITE);
            textLabel12.setFont(new Font("Monospaced", Font.BOLD, 18));
            textLabel12.setBounds(70, 610, 200, 20); // texto a la par

            leftPanel.add(marco1Label);
            leftPanel.add(marco2Label);
            leftPanel.add(itemLabel);
            leftPanel.add(itemLabel2);
            leftPanel.add(itemLabel3);
            leftPanel.add(itemLabel4);
            leftPanel.add(itemLabel5);
            leftPanel.add(itemLabel6);
            leftPanel.add(itemLabel7);
            leftPanel.add(itemLabel8);
            leftPanel.add(textLabel2);
            leftPanel.add(textLabel);
            leftPanel.add(textLabel3);
            leftPanel.add(textLabel4);
            leftPanel.add(textLabel5);
            leftPanel.add(textLabel6);
            leftPanel.add(textLabel7);
            leftPanel.add(textLabel8);
            leftPanel.add(textLabel9);
            leftPanel.add(textLabel10);
            leftPanel.add(textLabel11);
            leftPanel.add(textLabel12);

        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Error al cargar marco o ítem.");
        }

        mainPanel.add(leftPanel, BorderLayout.WEST);
    }
    public void updateGame(Juego juego) {
        gamePanel.setJugadores(juego.jugadores);
        gamePanel.setNivelActual(juego.nivelActual);
        gamePanel.repaint();
    }

    public void updateBloques(java.util.List<Bloque> bloques) {
        gamePanel.setBloques(bloques, 30, 187);
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

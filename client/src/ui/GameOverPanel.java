package ui;

import model.Fruta;
import model.Jugador;
import model.Obstacle;
import model.Pterodactilo;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

public class GameOverPanel extends JPanel {
    private final boolean dosJugadores;
    private final BufferedWriter output;

    private BufferedImage aveImg, hieloImg, yetiImg, popoImg, nanaImg;
    private BufferedImage bananaImg, eggplantImg, orangeImg, lettuceImg;

    public GameOverPanel(Jugador[] jugadores, List<Fruta> frutas, List<Obstacle> obstacles, Pterodactilo ptero, boolean dosJugadores, BufferedWriter output) {
        this.dosJugadores = dosJugadores;
        this.output = output;
        setLayout(null);
        setBackground(Color.BLACK);

        cargarImagenes();
        construirUI(jugadores);
        agregarBotones();

        if (dosJugadores) {
            agregarSeparadorVertical(540);
        }
    }

    private void cargarImagenes() {
        try {
            aveImg = ImageIO.read(getClass().getResource("/ui/figuras/aveN.png"));
            hieloImg = ImageIO.read(getClass().getResource("/ui/figuras/hielo.png"));
            yetiImg = ImageIO.read(getClass().getResource("/ui/figuras/yeti.png"));
            popoImg = ImageIO.read(getClass().getResource("/ui/figuras/popo.png"));
            nanaImg = ImageIO.read(getClass().getResource("/ui/figuras/nana.png"));
            bananaImg = ImageIO.read(getClass().getResource("/ui/figuras/banana.png"));
            eggplantImg = ImageIO.read(getClass().getResource("/ui/figuras/egg_plant.png"));
            orangeImg = ImageIO.read(getClass().getResource("/ui/figuras/orange.png"));
            lettuceImg = ImageIO.read(getClass().getResource("/ui/figuras/lettuce.png"));
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Error al cargar imágenes del panel Game Over: " + e.getMessage());
        }
    }

    private void construirUI(Jugador[] jugadores) {
        JLabel gameOverTitle = new JLabel("El juego ha finalizado", SwingConstants.CENTER);
        gameOverTitle.setForeground(Color.WHITE);
        gameOverTitle.setFont(new Font("Monospaced", Font.BOLD, 36));
        gameOverTitle.setBounds(0, 30, 800, 50);
        add(gameOverTitle);

        crearBloquesJugador(jugadores[0], 170, "JUGADOR 1", popoImg);

        if (dosJugadores && jugadores.length > 1 && jugadores[1] != null) {
            crearBloquesJugador(jugadores[1], 610, "JUGADOR 2", nanaImg);
        }
    }
    private void crearBloquesJugador(Jugador jugador, int xBase, String titulo, BufferedImage avatar) {
        int yInicio = 120;
        JLabel textLabel = new JLabel(titulo);
        textLabel.setForeground(Color.WHITE);
        textLabel.setFont(new Font("Monospaced", Font.BOLD, 20));
        textLabel.setBounds(xBase + 60, yInicio, 200, 20); // centrado sobre dos columnas
        add(textLabel);

        int col1X = xBase;
        int col2X = xBase + 160;
        int y1 = yInicio + 30;
        int y2 = yInicio + 30;

        y1 = addLineaConImagen(col1X, y1, hieloImg, jugador.puntos_hielo, 10);       // hielo
        y1 = addLineaConImagen(col1X, y1, aveImg, jugador.puntos_ave, 800);          // ave
        y1 = addLineaConImagen(col1X, y1, yetiImg, jugador.puntos_yeti, 400);        // yeti

        y2 = addLineaConImagen(col2X, y2, orangeImg, jugador.puntos_naranja, 100);   // naranja
        y2 = addLineaConImagen(col2X, y2, bananaImg, jugador.puntos_banano, 200);    // banana
        y2 = addLineaConImagen(col2X, y2, eggplantImg, jugador.puntos_berenjena, 300); // berenjena
        y2 = addLineaConImagen(col2X, y2, lettuceImg, jugador.puntos_lechuga, 400);    // lechuga

        int yFinal = Math.max(y1, y2) + 10;
        JLabel total = new JLabel("TOTAL: " + jugador.calcularPuntajeTotal());
        total.setForeground(Color.WHITE);
        total.setFont(new Font("Monospaced", Font.BOLD, 20));
        total.setBounds(xBase + 60, yFinal, 250, 30);
        add(total);
    }

    private int addLineaConImagen(int x, int y, BufferedImage img, int cantidad, int valorUnidad) {
        String texto = cantidad + " X " + valorUnidad;

        JLabel imgLabel = new JLabel(new ImageIcon(img.getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
        imgLabel.setBounds(x, y, 50, 50);
        add(imgLabel);

        JLabel label = new JLabel(texto);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Monospaced", Font.BOLD, 18));
        label.setBounds(x + 60, y + 15, 120, 20);
        add(label);

        return y + 50;
    }

    private void agregarSeparadorVertical(int x) {
        JLabel separator = new JLabel();
        separator.setOpaque(true);
        separator.setBackground(Color.GRAY);
        separator.setBounds(x, 100, 2, 360);
        add(separator);
    }

    private void agregarBotones() {
        Color teal = new Color(0, 180, 180);

        JButton volverBtn = new JButton("Volver al Menú");
        volverBtn.setBounds(220, 520, 180, 45);
        volverBtn.setFocusPainted(false);
        estilizarBoton(volverBtn, teal);
        volverBtn.addActionListener(e -> {
            try {
                if (output != null) {
                    output.write("CERRAR\n");
                    output.flush();
                }
            } catch (IOException ex) {
                System.err.println("Error al enviar CERRAR al servidor: " + ex.getMessage());
            }

            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.dispose();

            SwingUtilities.invokeLater(() -> {
                JFrame menu = new JFrame("iCE Climber");
                menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                menu.setSize(900, 700);
                menu.setLocationRelativeTo(null);
                menu.setContentPane(new MainMenuPanel(menu));
                menu.setVisible(true);
            });
        });

        JButton continuarBtn = new JButton("Continuar");
        continuarBtn.setBounds(420, 520, 180, 45);
        continuarBtn.setFocusPainted(false);
        estilizarBoton(continuarBtn, teal);
        continuarBtn.addActionListener(e -> {
            try {
                if (output != null) {
                    output.write("CONTINUAR\n");
                    output.flush();
                    System.out.println("Comando CONTINUAR enviado al servidor.");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        add(volverBtn);
        add(continuarBtn);
    }

    private void estilizarBoton(JButton boton, Color colorFondo) {
        boton.setBackground(colorFondo);
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Monospaced", Font.BOLD, 16));
        boton.setBorder(new RoundedBorder(30));
    }

    static class RoundedBorder extends AbstractBorder {
        private final int radius;

        RoundedBorder(int radius) {
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.setColor(Color.WHITE);
            g.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(8, 16, 8, 16);
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.left = insets.right = 16;
            insets.top = insets.bottom = 8;
            return insets;
        }
    }
}

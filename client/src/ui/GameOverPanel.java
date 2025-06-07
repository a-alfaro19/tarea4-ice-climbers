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
        int y = 120;
        JLabel textLabel = new JLabel(titulo);
        textLabel.setForeground(Color.WHITE);
        textLabel.setFont(new Font("Monospaced", Font.BOLD, 20));
        textLabel.setBounds(xBase + 10, y, 200, 20);
        add(textLabel);

        y += 30;
        addImagenTexto(xBase, y, hieloImg, "10 X 0");
        addFrutaALaPar(xBase + 160, y, 0);

        y += 50;
        addImagenTexto(xBase, y, aveImg, "800 X 0");
        addFrutaALaPar(xBase + 160, y, 1);

        y += 50;
        addImagenTexto(xBase, y, yetiImg, "400 X 0");
        addFrutaALaPar(xBase + 160, y, 2);

        y += 50;
        addImagenTexto(xBase, y, avatar, "");
        addFrutaALaPar(xBase + 160, y, 3);

        y += 50;
        JLabel total = new JLabel("TOTAL: " + jugador.puntaje);
        total.setForeground(Color.WHITE);
        total.setFont(new Font("Monospaced", Font.BOLD, 20));
        total.setBounds(xBase + 20, y, 200, 20);
        add(total);
    }

    private void addFrutaALaPar(int x, int y, int tipo) {
        BufferedImage img = switch (tipo) {
            case 0 -> orangeImg;
            case 1 -> bananaImg;
            case 2 -> eggplantImg;
            case 3 -> lettuceImg;
            default -> null;
        };

        int valor = switch (tipo) {
            case 0 -> 100;
            case 1 -> 200;
            case 2 -> 300;
            case 3 -> 400;
            default -> 0;
        };

        if (img != null) {
            JLabel frutaLabel = new JLabel(new ImageIcon(img.getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
            frutaLabel.setBounds(x, y, 50, 50);
            add(frutaLabel);

            JLabel text = new JLabel(valor + " X 0");
            text.setForeground(Color.WHITE);
            text.setFont(new Font("Monospaced", Font.BOLD, 18));
            text.setBounds(x + 55, y + 15, 100, 20);
            add(text);
        }
    }

    private void agregarSeparadorVertical(int x) {
        JLabel separator = new JLabel();
        separator.setOpaque(true);
        separator.setBackground(Color.GRAY);
        separator.setBounds(x, 100, 2, 280);
        add(separator);
    }

    private void agregarBotones() {
        Color teal = new Color(0, 180, 180);

        JButton volverBtn = new JButton("Volver al Menú");
        volverBtn.setBounds(220, 450, 180, 45);
        volverBtn.setFocusPainted(false);
        estilizarBoton(volverBtn, teal);
        volverBtn.addActionListener(e -> {
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
        continuarBtn.setBounds(420, 450, 180, 45);
        continuarBtn.setFocusPainted(false);
        estilizarBoton(continuarBtn, teal);
        continuarBtn.addActionListener(e -> {
            try {
                if (output != null) {
                    output.write("CONTINUAR\n");
                    output.flush();
                    System.out.println("Comando CONTINUAR enviado al servidor.");
                } else {
                    System.err.println("No se pudo continuar: output es null.");
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

    private void addImagenTexto(int x, int y, BufferedImage img, String texto) {
        JLabel imgLabel = new JLabel(new ImageIcon(img.getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
        imgLabel.setBounds(x, y, 50, 50);
        add(imgLabel);

        JLabel text = new JLabel(texto);
        text.setForeground(Color.WHITE);
        text.setFont(new Font("Monospaced", Font.BOLD, 20));
        text.setBounds(x + 60, y + 15, 200, 20);
        add(text);
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

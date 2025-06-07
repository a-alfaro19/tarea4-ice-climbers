package ui;

import model.Fruta;
import model.Jugador;
import model.Obstacle;
import model.Pterodactilo;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

public class GameOverPanel extends JPanel {
    private final boolean dosJugadores;
    private BufferedImage aveImg, hieloImg, yetiImg, popoImg, nanaImg;

    public GameOverPanel(Jugador[] jugadores, List<Fruta> frutas, List<Obstacle> obstacles, Pterodactilo ptero, boolean dosJugadores) {
        this.dosJugadores = dosJugadores;
        setLayout(null);
        setBackground(Color.BLACK);

        cargarImagenes();
        construirUI(jugadores);
    }

    private void cargarImagenes() {
        try {
            aveImg = ImageIO.read(getClass().getResource("/ui/figuras/aveN.png"));
            hieloImg = ImageIO.read(getClass().getResource("/ui/figuras/hielo.png"));
            yetiImg = ImageIO.read(getClass().getResource("/ui/figuras/yeti.png"));
            popoImg = ImageIO.read(getClass().getResource("/ui/figuras/popo.png"));
            nanaImg = ImageIO.read(getClass().getResource("/ui/figuras/nana.png"));
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Error al cargar imágenes del panel Game Over: " + e.getMessage());
        }
    }

    private void construirUI(Jugador[] jugadores) {
        JLabel gameOverTitle = new JLabel("El juego ha finalizado", SwingConstants.CENTER);
        gameOverTitle.setForeground(Color.WHITE);
        gameOverTitle.setFont(new Font("Monospaced", Font.BOLD, 36));
        gameOverTitle.setBounds(0, 50, 800, 50);
        add(gameOverTitle);

        crearBloquesJugador(jugadores[0], 170, "JUGADOR 1", popoImg);

        if (dosJugadores && jugadores.length > 1 && jugadores[1] != null) {
            crearBloquesJugador(jugadores[1], 470, "JUGADOR 2", nanaImg);
        }
    }

    private void crearBloquesJugador(Jugador jugador, int xBase, String titulo, BufferedImage avatar) {
        int y = 190;
        JLabel textLabel = new JLabel(titulo);
        textLabel.setForeground(Color.WHITE);
        textLabel.setFont(new Font("Monospaced", Font.BOLD, 20));
        textLabel.setBounds(xBase + 27, y, 200, 20);
        add(textLabel);

        y += 30;
        addImagenTexto(xBase, y, hieloImg, "10 X 0");
        y += 50;
        addImagenTexto(xBase, y, aveImg, "800 X 0");
        y += 50;
        addImagenTexto(xBase, y, yetiImg, "400 X 0");
        y += 50;
        addImagenTexto(xBase, y, avatar, "");

        y += 50;
        JLabel total = new JLabel("TOTAL: ");
        total.setForeground(Color.WHITE);
        total.setFont(new Font("Monospaced", Font.BOLD, 20));
        total.setBounds(xBase + 20, y, 200, 20);
        add(total);

        y += 50;
        JLabel bonus = new JLabel("BONUS: NO!");
        bonus.setForeground(Color.WHITE);
        bonus.setFont(new Font("Monospaced", Font.BOLD, 18));
        bonus.setBounds(xBase + 50, y, 200, 20);
        add(bonus);
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
}

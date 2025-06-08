package ui;

import client.PlayerClient;
import model.Bloque;
import model.Fruta;
import model.Juego;

import java.util.List;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import javax.imageio.ImageIO;
import java.io.IOException;

/**
 * Ventana principal del jugador en el juego iCE Climber.
 * Esta clase se encarga de crear la interfaz visual del cliente jugador y actualizar el estado del juego.
 */
public class GameWindow extends JFrame {

    /** Nombre del jugador que usa esta ventana. */
    private String nombreJugador = "";

    /** Panel grafico donde se renderiza el estado del juego. */
    private final GamePanel gamePanel;

    /** Referencia al cliente jugador que controla esta ventana. */
    private PlayerClient playerClient;

    /** Instancia unica de GameWindow. */
    private static GameWindow instancia;

    /**
     * Constructor de la ventana del juego.
     *
     * @param nombreJugador nombre del jugador asignado
     * @param output salida hacia el servidor
     * @param dosJugadores indica si el juego es de dos jugadores
     */
    public GameWindow(String nombreJugador, BufferedWriter output, Boolean dosJugadores) {
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

    /**
     * Actualiza el estado general del juego a partir del objeto Juego recibido del servidor.
     *
     * @param juego instancia con el estado actual del juego
     */
    public void updateGame(Juego juego) {
        gamePanel.setJugadores(juego.jugadores);
        gamePanel.setObstacles(juego.obstacles);
        gamePanel.setNivelActual(juego.nivelActual);
        gamePanel.setFrutas(juego.frutas);
        gamePanel.setPterodactilo(juego.pterodactilo);
        gamePanel.setAtrapoPtero(juego.atrapo_ptero);
        gamePanel.verificarSiTodosMuertos();
        gamePanel.verificarAtrapoPtero();
        gamePanel.repaint();
    }

    /**
     * Actualiza los bloques del mapa visibles por el jugador.
     *
     * @param bloques lista de bloques a mostrar
     */
    public void updateBloques(List<Bloque> bloques) {
        gamePanel.setBloques(bloques, 30, 89);
        gamePanel.repaint();
    }

    /**
     * Obtiene una referencia a la instancia de la ventana desde el cliente.
     * (Advertencia: si playerClient es null, esta llamada fallara.)
     */
    public void getWindow() {
        // Guardamos la instancia actual
        instancia = playerClient.getWindow();
    }

    /**
     * Cierra la ventana actual del juego si existe.
     */
    public void cerrarVentana() {
        if (instancia == null) {
            getWindow();
            cerrarVentana();
        }
        if (instancia != null) {
            instancia.dispose(); // Cierra la ventana
            instancia = null;
        }
    }

    /**
     * Panel interno que combina una imagen y una etiqueta de texto, usado para mostrar informacion visualmente.
     */
    class CharacterPanel extends JPanel {
        private BufferedImage image;
        private String label;

        /**
         * Constructor del panel con imagen y texto.
         *
         * @param resourcePath ruta al recurso de imagen
         * @param label texto a mostrar junto a la imagen
         */
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

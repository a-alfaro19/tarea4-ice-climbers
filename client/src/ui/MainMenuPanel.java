package ui;

import client.ClientFactory;
import client.ObserverClient;
import client.PlayerClient;
import observer.GameObserver;
import observer.ObserverWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

/**
 * Panel del menu principal del juego iCE Climber.
 * Permite al usuario seleccionar entre jugar, observar o salir.
 */
public class MainMenuPanel extends JPanel {

    /** Opciones del menu que el usuario puede seleccionar. */
    private final String[] options = {"JUGAR POPO", "2 JUGADORES", "OBSERVAR", "SALIR"};

    /** Indice de la opcion actualmente seleccionada. */
    private Integer selection = 0;

    /** Ventana principal que contiene este panel. */
    private final JFrame mainFrame;

    /** Indica si ya se ha iniciado una opcion para evitar duplicidad. */
    private Boolean started = false;

    /** Indica si se esta esperando al segundo jugador en modo de 2 jugadores. */
    private volatile Boolean esperandoSegundoJugador = false;

    /**
     * Constructor del panel del menu principal.
     * Configura comportamiento de teclado y apariencia visual.
     *
     * @param mainFrame ventana principal del juego
     */
    public MainMenuPanel(JFrame mainFrame) {
        this.mainFrame = mainFrame;

        setBackground(Color.BLACK);
        setFocusable(true);
        requestFocusInWindow();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP -> {
                        selection = (selection - 1 + options.length) % options.length;
                        repaint();
                    }
                    case KeyEvent.VK_DOWN -> {
                        selection = (selection + 1) % options.length;
                        repaint();
                    }
                    case KeyEvent.VK_ENTER -> executeOption();
                }
            }
        });
    }

    /**
     * Ejecuta la opcion actualmente seleccionada del menu.
     */
    private void executeOption() {
        if (started) return;
        String selected = options[selection];

        switch (selected) {
            case "JUGAR POPO" -> iniciarClienteJugador(false);
            case "2 JUGADORES" -> iniciarClienteJugador(true);
            case "OBSERVAR" -> iniciarObservador();
            case "SALIR" -> System.exit(0);
        }
    }

    /**
     * Inicia el cliente jugador en modo de 1 o 2 jugadores.
     *
     * @param esperarSegundoJugador true si se debe esperar al segundo jugador
     */
    private void iniciarClienteJugador(boolean esperarSegundoJugador) {
        started = true;
        new Thread(() -> {
            try {
                PlayerClient client = (PlayerClient) ClientFactory.createClient("PLAYER", "192.168.0.109", 8080);
                client.setDosJugadores(esperarSegundoJugador);
                client.identify();

                if (esperarSegundoJugador && client.getNombreJugador().equalsIgnoreCase("Popo")) {
                    esperandoSegundoJugador = true;
                    repaint();

                    while (true) {
                        String respuesta = client.getResponse();
                        if (respuesta.trim().equals("START")) break;
                    }

                    esperandoSegundoJugador = false;
                    repaint();
                }

                client.startListening();
                SwingUtilities.invokeLater(() -> mainFrame.dispose());

            } catch (IOException e) {
                started = false;
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(
                        this,
                        "Ya hay una partida activa, intente conectarse como Observador.\n" + e.getMessage(),
                        "Conexion fallida",
                        JOptionPane.ERROR_MESSAGE
                ));
            }
        }).start();
    }

    /**
     * Inicia el cliente observador y permite seleccionar a quien observar.
     */
    private void iniciarObservador() {
        started = true;
        new Thread(() -> {
            try {
                // Preguntar al usuario a quien desea observar
                String[] opciones = {"Popo", "Nana"};
                String seleccionado = (String) JOptionPane.showInputDialog(
                        this,
                        "¿A quien desea observar?",
                        "Seleccionar jugador",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        opciones,
                        opciones[0]
                );

                if (seleccionado == null) {
                    started = false;
                    return;
                }

                // Comando a enviar al servidor
                String comando = seleccionado.equalsIgnoreCase("Popo") ? "OBSERVER_POPO" : "OBSERVER_NANA";

                // Crear cliente observador
                ObserverClient client = (ObserverClient) ClientFactory.createClient("OBSERVER", "192.168.0.109", 8080);

                // Enviar comando y recibir asignacion
                String observado = client.identify(comando);

                // Crear ventana de observacion
                ObserverWindow observerWindow = new ObserverWindow(observado, client.isDosJugadores());
                client.addObserver(observerWindow);
                client.startListening();

                SwingUtilities.invokeLater(() -> mainFrame.dispose());

            } catch (IOException e) {
                started = false;
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(
                        this,
                        "El jugador seleccionado no esta en partida o ya llego al maximo de observadores.\n" + e.getMessage(),
                        "Conexion fallida",
                        JOptionPane.ERROR_MESSAGE
                ));
            }
        }).start();
    }

    /**
     * Dibuja los elementos visuales del panel: titulo, opciones del menu,
     * e indicador de espera del segundo jugador si aplica.
     *
     * @param g el contexto grafico para pintar
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int width = getWidth();
        int height = getHeight();

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 64));
        String title = "iCE CLIMBER";
        int titleWidth = g.getFontMetrics().stringWidth(title);
        g.drawString(title, (width - titleWidth) / 2, height / 4);

        g.setFont(new Font("Arial", Font.PLAIN, 36));
        int optionHeight = 50;
        int startY = height / 2 - (options.length * optionHeight) / 2;

        for (int i = 0; i < options.length; i++) {
            String text = (i == selection ? "> " : "  ") + options[i];
            g.setColor(i == selection ? Color.YELLOW : Color.WHITE);
            int textWidth = g.getFontMetrics().stringWidth(text);
            g.drawString(text, (width - textWidth) / 2, startY + i * optionHeight);
        }

        if (esperandoSegundoJugador) {
            g.setColor(new Color(200, 100, 255));
            g.setFont(new Font("Arial", Font.BOLD, 24));
            String texto = "Esperando conexion del segundo jugador...";
            int textWidth = g.getFontMetrics().stringWidth(texto);
            int textY = startY + options.length * optionHeight + 40;
            g.drawString(texto, (width - textWidth) / 2, textY);
        }
    }
}

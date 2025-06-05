package ui;

import client.ClientFactory;
import client.PlayerClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class MainMenuPanel extends JPanel {
    private final String[] options = {"JUGAR POPO", "2 JUGADORES", "OBSERVAR", "SALIR"};
    private int selection = 0;
    private final JFrame mainFrame;
    private boolean started = false;
    private volatile boolean esperandoSegundoJugador = false;


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

    private void iniciarClienteJugador(boolean esperarSegundoJugador) {
        started = true;
        new Thread(() -> {
            try {
                PlayerClient client = (PlayerClient) ClientFactory.createClient("PLAYER", "localhost", 8080);
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
                        "Error al iniciar el cliente jugador:\n" + e.getMessage(),
                        "Conexión fallida",
                        JOptionPane.ERROR_MESSAGE
                ));
            }
        }).start();
    }

    private void iniciarObservador() {
        started = true;
        new Thread(() -> {
            try {
                PlayerClient client = (PlayerClient) ClientFactory.createClient("OBSERVER", "localhost", 8080);
                client.identify();
                client.startListening();
                SwingUtilities.invokeLater(() -> mainFrame.dispose());
            } catch (IOException e) {
                started = false;
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(
                        this,
                        "Error al iniciar como observador:\n" + e.getMessage(),
                        "Conexión fallida",
                        JOptionPane.ERROR_MESSAGE
                ));
            }
        }).start();
    }

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
            g.setColor(new Color(200, 100, 255)); // morado claro
            g.setFont(new Font("Arial", Font.BOLD, 24));
            String texto = "Esperando conexión del segundo jugador...";
            int textWidth = g.getFontMetrics().stringWidth(texto);
            int textY = startY + options.length * optionHeight + 40;
            g.drawString(texto, (width - textWidth) / 2, textY);
        }

    }
}

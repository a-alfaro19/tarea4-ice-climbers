package observer;

import model.Bloque;
import model.Juego;
import model.Jugador;
import ui.GameOverPanel;
import ui.GamePanel;
import ui.MainMenuPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class ObserverWindow extends JFrame implements GameObserver {
    private final String observado;
    private final boolean dosJugadores;
    private final GamePanel panel;
    private boolean juegoTerminado = false; // para no mostrar GameOver múltiples veces

    public ObserverWindow(String observado, boolean dosJugadores) {
        this.observado = observado;
        this.dosJugadores = dosJugadores;

        setTitle("Observador de " + observado);
        setSize(1250, 660);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel de juego
        panel = new GamePanel(observado, null, dosJugadores, false);
        add(panel);
        setVisible(true);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                mostrarMenuObservadorFinalizado();
            }
        });
    }

    private void mostrarMenuObservadorFinalizado() {
        JOptionPane.showMessageDialog(
                null,
                "La observación ha terminado.",
                "Sesión finalizada",
                JOptionPane.INFORMATION_MESSAGE
        );

        SwingUtilities.invokeLater(() -> {
            JFrame nuevoFrame = new JFrame("iCE Climber");
            nuevoFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            nuevoFrame.setSize(800, 600);
            nuevoFrame.setLocationRelativeTo(null);
            nuevoFrame.setResizable(false);
            nuevoFrame.setContentPane(new MainMenuPanel(nuevoFrame));
            nuevoFrame.setVisible(true);
        });
    }

    @Override
    public void onGameUpdate(Juego juego, List<Bloque> bloques) {
        SwingUtilities.invokeLater(() -> {
            if (juegoTerminado) return;

            boolean todosMuertos = true;
            for (Jugador j : juego.jugadores) {
                if (j != null && j.vidas != null && j.vidas > 0) {
                    todosMuertos = false;
                    break;
                }
            }

            if (todosMuertos) {
                juegoTerminado = true;
                mostrarFinDeJuego(juego);
                return;
            }

            panel.setJugadores(juego.jugadores);
            panel.setNivelActual(juego.nivelActual);
            panel.setObstacles(juego.obstacles);
            panel.setFrutas(juego.frutas);
            panel.setPterodactilo(juego.pterodactilo);
            panel.setBloques(bloques, 30, 89);
            panel.repaint();
        });
    }

    private void mostrarFinDeJuego(Juego juego) {
        getContentPane().removeAll();
        JPanel panelGameOver = new GameOverPanel(juego.jugadores, juego.frutas, juego.obstacles, juego.pterodactilo, dosJugadores);
        add(panelGameOver);
        revalidate();
        repaint();
    }
}

package observer;

import model.Bloque;
import model.Juego;
import ui.GamePanel;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ObserverWindow extends JFrame implements GameObserver {
    private final GamePanel panel;
    private final String observado;

    public ObserverWindow(String observado, boolean dosJugadores) {
        this.observado = observado;
        setTitle("Observador de " + observado);
        setSize(1250, 660);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // usa el valor real de dosJugadores
        panel = new GamePanel(observado, null, dosJugadores, false);
        add(panel);
        setVisible(true);
    }

    @Override
    public void onGameUpdate(Juego juego, List<Bloque> bloques) {
        SwingUtilities.invokeLater(() -> {
            panel.setJugadores(juego.jugadores);
            panel.setNivelActual(juego.nivelActual);
            panel.setBloques(bloques, 30, 91);
            panel.repaint();
        });
    }
}

package observer;

import model.Juego;
import ui.GamePanel;

import javax.swing.*;
import java.awt.*;
public class ObserverWindow extends JFrame implements GameObserver {
    private final GamePanel panel;
    private final String observado; // "Popo" o "Nana"

    public ObserverWindow(String observado) {
        this.observado = observado;
        setTitle("Observador de " + observado);
        setSize(1250, 660);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // GamePanel se configura con nombre observado y modo solo visual
        panel = new GamePanel(observado, null, true, false);
        add(panel);
        setVisible(true);
    }

    @Override
    public void onGameUpdate(Juego juego) {
        SwingUtilities.invokeLater(() -> {
            panel.setJugadores(juego.jugadores);
            panel.setNivelActual(juego.nivelActual);
            panel.repaint();
        });
    }

}

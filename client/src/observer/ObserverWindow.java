package observer;

import model.Bloque;
import model.Juego;
import ui.GamePanel;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ObserverWindow extends JFrame implements GameObserver {
    private final GamePanel panel;
    private final String observado; // "Popo" o "Nana"

    public ObserverWindow(String observado) {
        this.observado = observado;
        setTitle("Observador de " + observado);
        setSize(1250, 660);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // GamePanel se configura con nombre observado y modo solo visual
        panel = new GamePanel(observado, null, true, false);
        add(panel);
        setVisible(true);
    }

    @Override
    public void onGameUpdate(Juego juego, List<Bloque> bloques) {
        SwingUtilities.invokeLater(() -> {
            panel.setJugadores(juego.jugadores);
            panel.setNivelActual(juego.nivelActual);
            panel.setBloques(bloques, 30, 91); // Asegúrate de usar el tamaño real del mapa
            panel.repaint();
        });
    }
}

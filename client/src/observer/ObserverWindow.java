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

/**
 * Ventana del cliente observador. Permite visualizar en tiempo real el avance del juego
 * desde la perspectiva de un jugador (Popo o Nana), sin posibilidad de interacción.
 */
public class ObserverWindow extends JFrame implements GameObserver {

    /** Nombre del jugador observado (Popo o Nana). */
    private final String observado;

    /** Indica si la partida es de dos jugadores. */
    private final Boolean dosJugadores;

    /** Panel de juego renderizado visualmente para el observador. */
    private final GamePanel panel;

    /** Control para evitar que el panel de Game Over se muestre más de una vez. */
    private Boolean juegoTerminado = false;

    /**
     * Crea una nueva ventana de observador para un jugador específico.
     *
     * @param observado nombre del jugador a observar ("Popo" o "Nana")
     * @param dosJugadores indica si la partida es multijugador
     */
    public ObserverWindow(String observado, Boolean dosJugadores) {
        this.observado = observado;
        this.dosJugadores = dosJugadores;

        setTitle("Observador de " + observado);
        setSize(1250, 660);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Crear panel de juego en modo no controlable (observador)
        panel = new GamePanel(observado, null, dosJugadores, false);
        add(panel);
        setVisible(true);

        // Al cerrar ventana, volver al menú principal
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                mostrarMenuObservadorFinalizado();
            }
        });
    }

    /**
     * Muestra una notificación de finalización y regresa al menú principal.
     * Se llama cuando el usuario cierra manualmente la ventana de observación.
     */
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

    /**
     * Metodo llamdo cuando el servidor envía una actualización del juego.
     * Se actualiza el panel visual y se verifica si la partida ha terminado.
     *
     * @param juego estado completo del juego
     * @param bloques bloques visibles en pantalla
     */
    @Override
    public void onGameUpdate(Juego juego, List<Bloque> bloques) {
        SwingUtilities.invokeLater(() -> {
            if (juegoTerminado) return;

            // Verificar si algún jugador atrapó al pterodáctilo
            if (juego.atrapo_ptero != null && juego.atrapo_ptero == 1) {
                juegoTerminado = true;
                mostrarFinDeJuego(juego);
                return;
            }

            // Verificar si todos los jugadores han muerto
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

            // Actualizar vista del juego
            panel.setJugadores(juego.jugadores);
            panel.setNivelActual(juego.nivelActual);
            panel.setObstacles(juego.obstacles);
            panel.setFrutas(juego.frutas);
            panel.setPterodactilo(juego.pterodactilo);
            panel.setBloques(bloques, 30, 89);
            panel.repaint();
        });
    }

    /**
     * Muestra el panel de Game Over cuando se detecta el fin de la partida.
     *
     * @param juego estado del juego al momento de finalizar
     */
    private void mostrarFinDeJuego(Juego juego) {
        getContentPane().removeAll();
        JPanel panelGameOver = new GameOverPanel(
                juego.jugadores,
                juego.frutas,
                juego.obstacles,
                juego.pterodactilo,
                dosJugadores,
                null // No hay output para observador
        );
        add(panelGameOver);
        revalidate();
        repaint();
    }
}

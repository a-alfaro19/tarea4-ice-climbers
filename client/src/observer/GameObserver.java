package observer;

import model.Bloque;
import model.Juego;
import java.util.List;

/**
 * Interfaz para observadores del juego iCE Climber.
 *
 * <p>Los objetos que implementan esta interfaz serán notificados cuando el estado
 * del juego cambie (por ejemplo, cuando se actualicen los jugadores, bloques,
 * obstáculos, frutas, etc.).</p>
 *
 * <p>Se usa principalmente para implementar el patrón Observer en la interfaz
 * gráfica de observadores remotos.</p>
 */
public interface GameObserver {

    /**
     * Metodo llamado cuando se recibe una actualización del estado del juego.
     *
     * @param juego estado completo del juego (jugadores, frutas, obstáculos, etc.)
     * @param bloques lista de bloques visibles en el mapa
     */
    void onGameUpdate(Juego juego, List<Bloque> bloques);
}

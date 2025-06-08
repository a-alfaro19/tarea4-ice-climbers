package observer;

import model.Bloque;
import model.Juego;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que implementa la lógica del patrón Observer para el juego iCE Climber.
 *
 * <p>Permite registrar observadores (como ventanas gráficas o componentes) que
 * desean recibir actualizaciones cuando cambia el estado del juego.</p>
 */
public class GameObservable {

    /** Lista de objetos observadores registrados. */
    private final List<GameObserver> observers = new ArrayList<>();

    /**
     * Agrega un nuevo observador a la lista de notificación.
     *
     * @param observer objeto que implementa {@link GameObserver}
     */
    public void addObserver(GameObserver observer) {
        observers.add(observer);
    }

    /**
     * Elimina un observador previamente registrado.
     *
     * @param observer observador a eliminar
     */
    public void removeObserver(GameObserver observer) {
        observers.remove(observer);
    }

    /**
     * Notifica a todos los observadores registrados con el estado actual del juego.
     *
     * @param juego objeto que contiene el estado completo del juego
     * @param bloques lista de bloques visibles en el mapa
     */
    public void notifyObservers(Juego juego, List<Bloque> bloques) {
        for (GameObserver observer : observers) {
            observer.onGameUpdate(juego, bloques);
        }
    }
}

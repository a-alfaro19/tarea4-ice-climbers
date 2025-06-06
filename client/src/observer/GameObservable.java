package observer;

import java.util.ArrayList;
import java.util.List;

public class GameObservable {
    private final List<GameObserver> observers = new ArrayList<>();

    public void addObserver(GameObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(GameObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers(model.Juego juego) {
        for (GameObserver observer : observers) {
            observer.onGameUpdate(juego);
        }
    }
}


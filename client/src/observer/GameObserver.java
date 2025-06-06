package observer;

import model.Juego;

public interface GameObserver {
    void onGameUpdate(Juego juego);
}

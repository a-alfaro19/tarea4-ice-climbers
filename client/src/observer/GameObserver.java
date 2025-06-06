package observer;

import model.Bloque;
import model.Juego;
import java.util.List;

public interface GameObserver {
    void onGameUpdate(Juego juego, List<Bloque> bloques);
}

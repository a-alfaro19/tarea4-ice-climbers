package model;

import java.io.DataInputStream;
import java.io.IOException;

public class Juego {
    public Jugador[] jugadores = new Jugador[2];
    public int nivelActual;
    public int enFaseBonus;
    public int velocidad;

    public static Juego readFrom(DataInputStream in) throws IOException {
        Juego juego = new Juego();
        for (int i = 0; i < 2; i++) {
            juego.jugadores[i] = Jugador.readFrom(in);
        }
        juego.nivelActual = in.readInt();
        juego.enFaseBonus = in.readInt();
        juego.velocidad = in.readInt();
        return juego;
    }
}

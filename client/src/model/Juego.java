package model;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Juego {
    public Jugador[] jugadores = new Jugador[2];
    public ArrayList<Obstacle> obstacles = new ArrayList<>();
    public Integer nivelActual;
    public Integer enFaseBonus;
    public Integer velocidad;

    public static Juego readFrom(DataInputStream in) throws IOException {
        Juego juego = new Juego();

        for (int i = 0; i < 2; i++) {
            juego.jugadores[i] = Jugador.readFrom(in);
        }

        // Receive Obstacles size
        int obstaclesSize = readIntLE(in);

        juego.obstacles.clear();
        for (int i = 0; i < obstaclesSize; i++) {
            juego.obstacles.add(Obstacle.readFrom(in));
        }

        juego.nivelActual = readIntLE(in);
        juego.enFaseBonus = readIntLE(in);
        juego.velocidad = readIntLE(in);

        return juego;
    }

    private static int readIntLE(DataInputStream in) throws IOException {
        int b1 = in.readUnsignedByte();
        int b2 = in.readUnsignedByte();
        int b3 = in.readUnsignedByte();
        int b4 = in.readUnsignedByte();
        return (b4 << 24) | (b3 << 16) | (b2 << 8) | b1;
    }
}


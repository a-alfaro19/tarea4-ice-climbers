package model;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ArrayList;

public class Juego {
    public Jugador[] jugadores = new Jugador[2];
    public ArrayList<Obstacle> obstacles = new ArrayList<>();
    public List<Fruta> frutas = new ArrayList<>();
    public Integer nivelActual;
    public Integer enFaseBonus;
    public Integer velocidad;
    public Pterodactilo pterodactilo;
    public Integer atrapo_ptero;


    public static Juego readFrom(DataInputStream in) throws IOException {
        Juego juego = new Juego();

        for (int i = 0; i < 2; i++) {
            juego.jugadores[i] = Jugador.readFrom(in);
        }

        // Receive Obstacles size
        Integer obstaclesSize = readIntLE(in);

        juego.obstacles.clear();
        for (int i = 0; i < obstaclesSize; i++) {
            juego.obstacles.add(Obstacle.readFrom(in));
        }
        // Leer frutas
        Integer cantidadFrutas = readIntLE(in);
        juego.frutas.clear();
        for (int i = 0; i < cantidadFrutas; i++) {
            juego.frutas.add(Fruta.readFrom(in));
        }

        juego.nivelActual = readIntLE(in);
        juego.enFaseBonus = readIntLE(in);
        juego.velocidad = readIntLE(in);
        juego.pterodactilo = Pterodactilo.readFrom(in);
        juego.atrapo_ptero = readIntLE(in);
        return juego;
    }

    static Integer readIntLE(DataInputStream in) throws IOException {
        int b1 = in.readUnsignedByte();
        int b2 = in.readUnsignedByte();
        int b3 = in.readUnsignedByte();
        int b4 = in.readUnsignedByte();
        return b4 << 24 | (b3 << 16) | (b2 << 8) | b1;
    }
}


package model;

import ui.Tile;

import java.io.DataInputStream;
import java.io.IOException;

public class Game {
    public Jugador[] players = new Jugador[2];
    public Integer nivelActual;
    public Integer enFaseBonus;
    public Integer velocidad;
    private Tile[][] map;

    public void setMap(Tile[][] map) {
        this.map = map;
    }

    public Tile[][] getMap() {
        return this.map;
    }

    public static Game readFrom(DataInputStream in) throws IOException {
        Game game = new Game();

        for (int i = 0; i < 2; i++) {
            game.players[i] = Jugador.readFrom(in);
        }

        game.nivelActual = readIntLE(in);
        game.enFaseBonus = readIntLE(in);
        game.velocidad = readIntLE(in);

        return game;
    }

    private static int readIntLE(DataInputStream in) throws IOException {
        int b1 = in.readUnsignedByte();
        int b2 = in.readUnsignedByte();
        int b3 = in.readUnsignedByte();
        int b4 = in.readUnsignedByte();
        return (b4 << 24) | (b3 << 16) | (b2 << 8) | b1;
    }
}


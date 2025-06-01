package model;

import java.io.DataInputStream;
import java.io.IOException;

public class Jugador {
    public String nombre;
    public int x, y;
    public int vidas;
    public int puntaje;
    public char direccion;

    public static Jugador readFrom(DataInputStream in) throws IOException {
        byte[] nombreBytes = new byte[10];
        in.readFully(nombreBytes);
        String nombre = new String(nombreBytes).trim();

        Jugador j = new Jugador();
        j.nombre = nombre;
        j.x = in.readInt();
        j.y = in.readInt();
        j.vidas = in.readInt();
        j.puntaje = in.readInt();
        j.direccion = in.readChar();
        return j;
    }
}

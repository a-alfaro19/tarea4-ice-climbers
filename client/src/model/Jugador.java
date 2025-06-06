package model;

import java.io.DataInputStream;
import java.io.IOException;

public class Jugador {
    public String nombre;
    public Integer x, y, vidas, puntaje;
    public float vy;
    public int en_el_aire;
    public Character direccion;
    public boolean activo = false;  // ← NUEVO CAMPO

    public static Jugador readFrom(DataInputStream in) throws IOException {
        Jugador j = new Jugador();

        byte[] nombreBytes = new byte[10];
        in.readFully(nombreBytes);
        j.nombre = new String(nombreBytes).trim();

        j.x = readIntLE(in);
        j.y = readIntLE(in);
        j.vidas = readIntLE(in);
        j.puntaje = readIntLE(in);

        // Leer float vy (4 bytes LE)
        j.vy = readFloatLE(in);
        // Leer en_el_aire (int, 4 bytes LE)
        j.en_el_aire = readIntLE(in);

        byte dir = in.readByte();
        j.direccion = (char) dir;

        byte relleno = in.readByte();

        // Establecer 'activo' automáticamente si tiene vidas y posición válida
        j.activo = (j.vidas != null && j.vidas > 0) || (j.x != null && j.y != null && (j.x != 0 || j.y != 0));

        return j;
    }

    private static int readIntLE(DataInputStream in) throws IOException {
        int b1 = in.readUnsignedByte();
        int b2 = in.readUnsignedByte();
        int b3 = in.readUnsignedByte();
        int b4 = in.readUnsignedByte();
        return (b4 << 24) | (b3 << 16) | (b2 << 8) | b1;
    }

    private static float readFloatLE(DataInputStream in) throws IOException {
        int intBits = readIntLE(in);
        return Float.intBitsToFloat(intBits);
    }
}





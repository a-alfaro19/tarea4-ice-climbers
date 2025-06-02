package model;

import java.io.DataInputStream;
import java.io.IOException;

public class Jugador {
    public String nombre;
    public Integer x, y, vidas, puntaje;
    public Character direccion;

    public static Jugador readFrom(DataInputStream in) throws IOException {
        Jugador j = new Jugador();

        byte[] nombreBytes = new byte[10];
        in.readFully(nombreBytes);
        //printBytes("nombre", nombreBytes);
        j.nombre = new String(nombreBytes).trim();

        j.x = readIntLE(in);  //printInt("x", j.x);
        j.y = readIntLE(in);  //printInt("y", j.y);
        j.vidas = readIntLE(in);  //printInt("vidas", j.vidas);
        j.puntaje = readIntLE(in); //printInt("puntaje", j.puntaje);

        byte dir = in.readByte();
        j.direccion = (char) dir;
        //System.out.printf("[direccion] 1 byte: %02X (%c)%n", dir, (char) dir);

        byte relleno = in.readByte();
        //System.out.printf("[relleno] 1 byte: %02X%n", relleno);

        return j;
    }

    private static int readIntLE(DataInputStream in) throws IOException {
        int b1 = in.readUnsignedByte();
        int b2 = in.readUnsignedByte();
        int b3 = in.readUnsignedByte();
        int b4 = in.readUnsignedByte();
        return (b4 << 24) | (b3 << 16) | (b2 << 8) | b1;
    }

/*    private static void printBytes(String label, byte[] data) {
        System.out.printf("[%s] %d bytes: ", label, data.length);
        for (byte b : data) {
            System.out.printf("%02X ", b);
        }
        System.out.println();
    }*/

/*    private static void printInt(String label, int value) {
        System.out.printf("[%s] 4 bytes: %02X %02X %02X %02X (%d)%n", label,
                (value >> 0) & 0xFF,
                (value >> 8) & 0xFF,
                (value >> 16) & 0xFF,
                (value >> 24) & 0xFF,
                value);
    }*/
}



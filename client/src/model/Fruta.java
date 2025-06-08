package model;

import java.io.DataInputStream;
import java.io.IOException;

public class Fruta {
    public Integer x, y, tipo, activa;

    public static Fruta readFrom(DataInputStream in) throws IOException {
        Fruta fruta = new Fruta();
        fruta.x = readIntLE(in);
        fruta.y = readIntLE(in);
        fruta.tipo = readIntLE(in);
        fruta.activa = readIntLE(in);
        return fruta;
    }

    private static int readIntLE(DataInputStream in) throws IOException {
        int b1 = in.readUnsignedByte();
        int b2 = in.readUnsignedByte();
        int b3 = in.readUnsignedByte();
        int b4 = in.readUnsignedByte();
        return (b4 << 24) | (b3 << 16) | (b2 << 8) | b1;
    }
}

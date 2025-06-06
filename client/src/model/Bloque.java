package model;

import java.io.DataInputStream;
import java.io.IOException;

public class Bloque {
    public Integer x, y, tipo, activo;

    public static Bloque readFrom(DataInputStream in) throws IOException {
        Bloque b = new Bloque();
        b.x = readIntLE(in);
        b.y = readIntLE(in);
        b.tipo = readIntLE(in);
        b.activo = readIntLE(in);
        return b;
    }

    private static int readIntLE(DataInputStream in) throws IOException {
        int b1 = in.readUnsignedByte();
        int b2 = in.readUnsignedByte();
        int b3 = in.readUnsignedByte();
        int b4 = in.readUnsignedByte();
        return (b4 << 24) | (b3 << 16) | (b2 << 8) | b1;
    }
}


package ui;
import java.io.DataInputStream;
import java.io.IOException;

public class Bloque {
    public int id;
    public int x;
    public int y;
    public int vida;
    public int destruido;
    public static Bloque readFrom(DataInputStream dis) throws IOException {
        Bloque b = new Bloque();
        b.id = dis.readInt();
        b.x = dis.readInt();
        b.y = dis.readInt();
        b.vida = dis.readInt();
        b.destruido = dis.readInt();
        return b;
    }
}

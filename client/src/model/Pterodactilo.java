package model;

import java.io.DataInputStream;
import java.io.IOException;

public class Pterodactilo {
    public Integer activo, x, y, direccion;

    public static Pterodactilo readFrom(DataInputStream in) throws IOException {
        Pterodactilo p = new Pterodactilo();
        p.activo = Juego.readIntLE(in);
        p.x = Juego.readIntLE(in);
        p.y = Juego.readIntLE(in);
        p.direccion = Juego.readIntLE(in);
        return p;
    }
}


package model;

import java.io.DataInputStream;
import java.io.IOException;

public class Jugador {
    public String nombre;
    public Integer x, y, vidas, puntaje;
    public Float vy;
    public Integer en_el_aire;
    public Character direccion;
    public Boolean activo = false;

    // Cantidades de objetos recolectados o destruidos
    public Integer puntos_hielo;
    public Integer puntos_ave;
    public Integer puntos_yeti;
    public Integer puntos_naranja;
    public Integer puntos_banano;
    public Integer puntos_berenjena;
    public Integer puntos_lechuga;

    public static Jugador readFrom(DataInputStream in) throws IOException {
        Jugador j = new Jugador();

        byte[] nombreBytes = new byte[10];
        in.readFully(nombreBytes);
        j.nombre = new String(nombreBytes).trim();

        j.x = readIntLE(in);
        j.y = readIntLE(in);
        j.vidas = readIntLE(in);
        j.puntaje = readIntLE(in);

        j.vy = readFloatLE(in);
        j.en_el_aire = readIntLE(in);
        byte dir = in.readByte();
        j.direccion = (char) dir;

        j.activo = (j.vidas != null && j.vidas > 0) || (j.x != null && j.y != null && (j.x != 0 || j.y != 0));
        in.readByte(); // relleno

        // Cantidad de objetos destruidos/recolectados
        j.puntos_hielo = readIntLE(in);
        j.puntos_ave = readIntLE(in);
        j.puntos_yeti = readIntLE(in);
        j.puntos_naranja = readIntLE(in);
        j.puntos_banano = readIntLE(in);
        j.puntos_berenjena = readIntLE(in);
        j.puntos_lechuga = readIntLE(in);

        return j;
    }

    // Cálculo de puntaje total real
    public Integer calcularPuntajeTotal() {
        return puntos_hielo * 10 +
                puntos_ave * 800 +
                puntos_yeti * 400 +
                puntos_naranja * 100 +
                puntos_banano * 200 +
                puntos_berenjena * 300 +
                puntos_lechuga * 400;
    }

    private static Integer readIntLE(DataInputStream in) throws IOException {
        int b1 = in.readUnsignedByte();
        int b2 = in.readUnsignedByte();
        int b3 = in.readUnsignedByte();
        int b4 = in.readUnsignedByte();
        return (b4 << 24) | (b3 << 16) | (b2 << 8) | b1;
    }

    private static Float readFloatLE(DataInputStream in) throws IOException {
        int intBits = readIntLE(in);
        return Float.intBitsToFloat(intBits);
    }
}

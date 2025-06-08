package model;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * Representa una fruta dentro del juego iCE Climber.
 *
 * <p>Las frutas otorgan puntaje adicional cuando son recolectadas por los jugadores durante la fase bonus.</p>
 */
public class Fruta {

    /** Posición horizontal de la fruta (columna en el mapa). */
    public Integer x;

    /** Posición vertical de la fruta (fila en el mapa). */
    public Integer y;

    /** Tipo de fruta (0=naranja, 1=banano, 2=berenjena, 3=lechuga). */
    public Integer tipo;

    /** Estado de la fruta: 1 si está activa (visible y recolectable), 0 si no. */
    public Integer activa;

    /**
     * Lee una fruta desde un flujo binario, asignando sus propiedades.
     *
     * @param in flujo de datos de entrada
     * @return objeto {@code Fruta} cargado desde el flujo
     * @throws IOException si ocurre un error durante la lectura
     */
    public static Fruta readFrom(DataInputStream in) throws IOException {
        Fruta fruta = new Fruta();
        fruta.x = readIntLE(in);
        fruta.y = readIntLE(in);
        fruta.tipo = readIntLE(in);
        fruta.activa = readIntLE(in);
        return fruta;
    }

    /**
     * Lee un entero en formato Little Endian desde el flujo de entrada.
     *
     * @param in flujo de entrada
     * @return entero reconstruido
     * @throws IOException si ocurre un error de lectura
     */
    private static int readIntLE(DataInputStream in) throws IOException {
        int b1 = in.readUnsignedByte();
        int b2 = in.readUnsignedByte();
        int b3 = in.readUnsignedByte();
        int b4 = in.readUnsignedByte();
        return (b4 << 24) | (b3 << 16) | (b2 << 8) | b1;
    }
}

package model;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * Representa un bloque dentro del mapa del juego iCE Climber.
 *
 * <p>Los bloques pueden ser de distintos tipos (destructibles, fijos, vacíos) y controlan el mapa jugable.</p>
 */
public class Bloque {

    /** Posición horizontal del bloque en el mapa. */
    public Integer x;

    /** Posición vertical del bloque en el mapa. */
    public Integer y;

    /** Tipo del bloque (según codificación definida en {@link TileType}). */
    public Integer tipo;

    /** Estado del bloque: 1 si está activo (presente), 0 si ha sido destruido o eliminado. */
    public Integer activo;

    /**
     * Lee un bloque desde un flujo binario en orden Little Endian.
     *
     * @param in flujo de entrada de datos
     * @return instancia del bloque cargada desde el flujo
     * @throws IOException si ocurre un error de lectura
     */
    public static Bloque readFrom(DataInputStream in) throws IOException {
        Bloque b = new Bloque();
        b.x = readIntLE(in);
        b.y = readIntLE(in);
        b.tipo = readIntLE(in);
        b.activo = readIntLE(in);
        return b;
    }

    /**
     * Lee un entero en formato Little Endian desde el flujo de entrada.
     *
     * @param in flujo de entrada binaria
     * @return entero reconstruido
     * @throws IOException si ocurre un error durante la lectura
     */
    private static int readIntLE(DataInputStream in) throws IOException {
        int b1 = in.readUnsignedByte();
        int b2 = in.readUnsignedByte();
        int b3 = in.readUnsignedByte();
        int b4 = in.readUnsignedByte();
        return (b4 << 24) | (b3 << 16) | (b2 << 8) | b1;
    }
}

package model;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * Representa a un jugador en el juego iCE Climber.
 *
 * <p>Incluye su estado actual (posición, vidas, puntaje), dirección, si está en el aire,
 * y su desempeño recolectando o destruyendo objetos.</p>
 */
public class Jugador {

    /** Nombre del jugador (Popo o Nana). */
    public String nombre;

    /** Posición horizontal en el mapa. */
    public Integer x;

    /** Posición vertical en el mapa. */
    public Integer y;

    /** Cantidad de vidas restantes. */
    public Integer vidas;

    /** Puntaje acumulado del jugador. */
    public Integer puntaje;

    /** Velocidad vertical (usada para simular el salto y gravedad). */
    public Float vy;

    /** Indica si está en el aire (1) o no (0). */
    public Integer en_el_aire;

    /** Dirección en la que está mirando o moviéndose ('L' o 'R'). */
    public Character direccion;

    /** Bandera que indica si el jugador está activo en el juego. */
    public Boolean activo = false;

    // Cantidades de objetos recolectados o destruidos por tipo:
    public Integer puntos_hielo;
    public Integer puntos_ave;
    public Integer puntos_yeti;
    public Integer puntos_naranja;
    public Integer puntos_banano;
    public Integer puntos_berenjena;
    public Integer puntos_lechuga;

    /**
     * Lee un jugador desde un flujo binario (proveniente del servidor).
     *
     * @param in flujo de entrada de datos
     * @return instancia de {@code Jugador} con sus datos cargados
     * @throws IOException si ocurre un error de lectura
     */
    public static Jugador readFrom(DataInputStream in) throws IOException {
        Jugador j = new Jugador();

        // Leer nombre
        byte[] nombreBytes = new byte[10];
        in.readFully(nombreBytes);
        j.nombre = new String(nombreBytes).trim();

        // Leer atributos principales
        j.x = readIntLE(in);
        j.y = readIntLE(in);
        j.vidas = readIntLE(in);
        j.puntaje = readIntLE(in);

        j.vy = readFloatLE(in);
        j.en_el_aire = readIntLE(in);

        byte dir = in.readByte();
        j.direccion = (char) dir;

        // Se considera activo si tiene vidas o una posición válida
        j.activo = (j.vidas != null && j.vidas > 0) || (j.x != null && j.y != null && (j.x != 0 || j.y != 0));
        in.readByte(); // leer byte de relleno

        // Leer contadores de objetos
        j.puntos_hielo = readIntLE(in);
        j.puntos_ave = readIntLE(in);
        j.puntos_yeti = readIntLE(in);
        j.puntos_naranja = readIntLE(in);
        j.puntos_banano = readIntLE(in);
        j.puntos_berenjena = readIntLE(in);
        j.puntos_lechuga = readIntLE(in);

        return j;
    }

    /**
     * Calcula el puntaje total del jugador con base en los objetos recolectados o destruidos.
     *
     * @return puntaje total acumulado
     */
    public Integer calcularPuntajeTotal() {
        return puntos_hielo * 10 +
                puntos_ave * 800 +
                puntos_yeti * 400 +
                puntos_naranja * 100 +
                puntos_banano * 200 +
                puntos_berenjena * 300 +
                puntos_lechuga * 400;
    }

    /**
     * Lee un entero en formato Little Endian desde el flujo de datos.
     *
     * @param in flujo de entrada
     * @return entero reconstruido
     * @throws IOException si ocurre un error de lectura
     */
    private static Integer readIntLE(DataInputStream in) throws IOException {
        int b1 = in.readUnsignedByte();
        int b2 = in.readUnsignedByte();
        int b3 = in.readUnsignedByte();
        int b4 = in.readUnsignedByte();
        return (b4 << 24) | (b3 << 16) | (b2 << 8) | b1;
    }

    /**
     * Lee un número flotante (float) en formato Little Endian desde el flujo de datos.
     *
     * @param in flujo de entrada
     * @return valor flotante reconstruido
     * @throws IOException si ocurre un error de lectura
     */
    private static Float readFloatLE(DataInputStream in) throws IOException {
        int intBits = readIntLE(in);
        return Float.intBitsToFloat(intBits);
    }
}

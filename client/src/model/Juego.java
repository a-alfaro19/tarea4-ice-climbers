package model;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa el estado completo del juego iCE Climber en un momento dado.
 *
 * <p>Contiene la información de los jugadores, obstáculos, frutas, nivel actual,
 * fase bonus, velocidad y el pterodáctilo.</p>
 */
public class Juego {

    /** Arreglo con los dos jugadores (Popo y Nana). */
    public Jugador[] jugadores = new Jugador[2];

    /** Lista de obstáculos activos en el mapa. */
    public ArrayList<Obstacle> obstacles = new ArrayList<>();

    /** Lista de frutas disponibles actualmente en el juego. */
    public List<Fruta> frutas = new ArrayList<>();

    /** Número del nivel actual. */
    public Integer nivelActual;

    /** Indica si el juego está en fase bonus (1) o no (0). */
    public Integer enFaseBonus;

    /** Velocidad actual del juego (influye en dificultad). */
    public Integer velocidad;

    /** Instancia del pterodáctilo, si está presente. */
    public Pterodactilo pterodactilo;

    /** Indica si un jugador atrapó al pterodáctilo (1) o no (0). */
    public Integer atrapo_ptero;

    /**
     * Lee el estado completo del juego desde un flujo binario.
     *
     * @param in flujo de entrada de datos
     * @return instancia del juego reconstruida
     * @throws IOException si ocurre un error durante la lectura
     */
    public static Juego readFrom(DataInputStream in) throws IOException {
        Juego juego = new Juego();

        // Leer los dos jugadores
        for (int i = 0; i < 2; i++) {
            juego.jugadores[i] = Jugador.readFrom(in);
        }

        // Leer cantidad de obstáculos
        int obstaclesSize = readIntLE(in);
        juego.obstacles.clear();
        for (int i = 0; i < obstaclesSize; i++) {
            juego.obstacles.add(Obstacle.readFrom(in));
        }

        // Leer cantidad de frutas
        int cantidadFrutas = readIntLE(in);
        juego.frutas.clear();
        for (int i = 0; i < cantidadFrutas; i++) {
            juego.frutas.add(Fruta.readFrom(in));
        }

        // Leer otros atributos del juego
        juego.nivelActual = readIntLE(in);
        juego.enFaseBonus = readIntLE(in);
        juego.velocidad = readIntLE(in);
        juego.pterodactilo = Pterodactilo.readFrom(in);
        juego.atrapo_ptero = readIntLE(in);

        return juego;
    }

    /**
     * Lee un entero en formato Little Endian desde un flujo de entrada.
     *
     * @param in flujo de entrada
     * @return entero reconstruido
     * @throws IOException si ocurre un error de lectura
     */
    static Integer readIntLE(DataInputStream in) throws IOException {
        int b1 = in.readUnsignedByte();
        int b2 = in.readUnsignedByte();
        int b3 = in.readUnsignedByte();
        int b4 = in.readUnsignedByte();
        return (b4 << 24) | (b3 << 16) | (b2 << 8) | b1;
    }
}

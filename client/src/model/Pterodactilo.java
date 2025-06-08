package model;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * Representa al pterodáctilo en el juego iCE Climber.
 *
 * <p>Es una criatura y que puede ser atrapada
 * por los jugadores para ganar el juego.</p>
 */
public class Pterodactilo {

    /** Indica si el pterodáctilo está activo (1) o no (0). */
    public Integer activo;

    /** Posición horizontal en el mapa. */
    public Integer x;

    /** Posición vertical en el mapa. */
    public Integer y;

    /** Dirección de movimiento: -1 para izquierda, 1 para derecha. */
    public Integer direccion;

    /**
     * Lee los datos del pterodáctilo desde un flujo binario (proveniente del servidor).
     *
     * @param in flujo de entrada de datos
     * @return instancia de {@code Pterodactilo} con sus valores cargados
     * @throws IOException si ocurre un error de lectura
     */
    public static Pterodactilo readFrom(DataInputStream in) throws IOException {
        Pterodactilo p = new Pterodactilo();
        p.activo = Juego.readIntLE(in);
        p.x = Juego.readIntLE(in);
        p.y = Juego.readIntLE(in);
        p.direccion = Juego.readIntLE(in);
        return p;
    }
}

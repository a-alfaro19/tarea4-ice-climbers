package client;

import observer.GameObserver;

import java.io.IOException;

/**
 * Interfaz que define las operaciones básicas que debe implementar un cliente en el juego iCE Climber.
 * <p>
 * Permite identificación, envío y recepción de mensajes, y  gestión de observadores en el caso del cliente observador.
 * </p>
 */
public interface IClient {

    /**
     * Envía la identificación del cliente al servidor.
     * <p>
     * El tipo de identificación depende del cliente (por ejemplo: "PLAYER1", "PLAYER2", "OBSERVER_POPO").
     * </p>
     *
     * @return cadena con el resultado de la identificación, o nombre asignado según el caso
     * @throws IOException si ocurre un error durante la comunicación
     */
    String identify() throws IOException;

    /**
     * Envía una solicitud textual al servidor.
     *
     * @param request cadena con el mensaje de solicitud (por ejemplo: comandos como "MOVER:L")
     * @throws IOException si ocurre un error al escribir al servidor
     */
    void sendRequest(String request) throws IOException;

    /**
     * Recibe la respuesta del servidor.
     *
     * @return cadena con la respuesta del servidor
     * @throws IOException si ocurre un error al leer desde el servidor
     */
    String getResponse() throws IOException;

    /**
     * Metodo  utilizado solo por los clientes observadores para registrar observadores del juego.
     *
     * @param observer instancia de {@link GameObserver} a registrar
     */
    default void addObserver(GameObserver observer) {

    }
}

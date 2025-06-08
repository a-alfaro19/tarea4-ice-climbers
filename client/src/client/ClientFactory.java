package client;

import java.io.IOException;

/**
 * Fábrica de clientes para el juego iCE Climber.
 * <p>Permite crear instancias de {@link PlayerClient} o {@link ObserverClient}.</p>
 */
public class ClientFactory {

    /**
     * Crea una instancia de cliente según el tipo especificado.
     *
     * @param type tipo de cliente a crear: "PLAYER" o "OBSERVER"
     * @param host dirección IP o nombre del host del servidor
     * @param port puerto de conexión al servidor
     * @return una instancia de {@link PlayerClient} o {@link ObserverClient}, implementando {@link IClient}
     * @throws IOException si ocurre un error al establecer la conexión
     * @throws IllegalArgumentException si el tipo proporcionado no es válido
     */
    public static IClient createClient(String type, String host, int port) throws IOException {
        return switch (type.toUpperCase()) {
            case "PLAYER" -> new PlayerClient(host, port);
            case "OBSERVER" -> new ObserverClient(host, port);
            default -> throw new IllegalArgumentException("Invalid Client Type: " + type);
        };
    }
}

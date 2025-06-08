package client;

import model.Bloque;
import model.Juego;
import observer.GameObservable;
import observer.GameObserver;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Cliente que actúa como observador del juego iCE Climber.
 *
 * <p>Se conecta al servidor para observar el estado del juego sin participar activamente.</p>
 * <p>Notifica a los {@link GameObserver} registrados cada vez que llega una nueva actualización del juego.</p>
 */
public class ObserverClient extends Client {

    /** Observador interno que permite manejar múltiples vistas. */
    private final GameObservable observable = new GameObservable();

    /** Indica si el juego observado es en modo dos jugadores. */
    private Boolean dosJugadores = false;

    /**
     * Crea un nuevo cliente observador.
     *
     * @param host dirección IP del servidor
     * @param port puerto de conexión
     * @throws IOException si no se puede establecer la conexión
     */
    public ObserverClient(String host, int port) throws IOException {
        super(host, port);
    }

    /**
     * Identifica al cliente ante el servidor como observador genérico.
     *
     * @return el nombre del jugador que se está observando ("Popo" o "Nana")
     * @throws IOException si la conexión fue rechazada
     */
    @Override
    public String identify() throws IOException {
        return identify("OBSERVER");
    }

    /**
     * Identifica al observador indicando a quién observar específicamente.
     *
     * @param tipoObservador "OBSERVER_POPO" o "OBSERVER_NANA"
     * @return el nombre del jugador que se está observando
     * @throws IOException si la conexión fue rechazada
     */
    public String identify(String tipoObservador) throws IOException {
        out.write((tipoObservador + "\n").getBytes());
        out.flush();

        // Leer respuesta del servidor
        StringBuilder response = new StringBuilder();
        char ch;
        while ((ch = (char) in.readByte()) != '\n') {
            response.append(ch);
        }

        if (!"ACCEPTED".equals(response.toString().trim())) {
            throw new IOException("\nIntente más tarde o con otro jugador.");
        }

        // Leer ID del jugador observado
        int observandoA = readIntLE(in);

        // Leer modo de juego (1 o 2 jugadores)
        this.dosJugadores = in.readUnsignedByte() == 1;

        return (observandoA == 0) ? "Popo" : "Nana";
    }

    /**
     * Devuelve si la partida observada es en modo de dos jugadores.
     *
     * @return {@code true} si es modo dos jugadores
     */
    public boolean isDosJugadores() {
        return dosJugadores;
    }

    /**
     * Registra un observador para recibir notificaciones del estado del juego.
     *
     * @param observer instancia que implementa {@link GameObserver}
     */
    public void addObserver(GameObserver observer) {
        observable.addObserver(observer);
    }

    /**
     * Comienza a escuchar actualizaciones del servidor en un hilo separado.
     * Cada vez que se recibe el estado del juego, se notifica a los observadores registrados.
     */
    public void startListening() {
        new Thread(() -> {
            try {
                while (true) {
                    Juego juego = Juego.readFrom(in);
                    int cantidad = readIntLE(in);
                    List<Bloque> bloques = new ArrayList<>();
                    for (int i = 0; i < cantidad; i++) {
                        bloques.add(Bloque.readFrom(in));
                    }
                    observable.notifyObservers(juego, bloques);
                }
            } catch (IOException e) {
                System.err.println("Conexión perdida (observador): " + e.getMessage());
            }
        }).start();
    }

    /**
     * Lee un entero en formato Little Endian desde un {@link DataInputStream}.
     *
     * @param in flujo de entrada binaria
     * @return entero reconstruido
     * @throws IOException si ocurre un error de lectura
     */
    private int readIntLE(DataInputStream in) throws IOException {
        int b1 = in.readUnsignedByte();
        int b2 = in.readUnsignedByte();
        int b3 = in.readUnsignedByte();
        int b4 = in.readUnsignedByte();
        return (b4 << 24) | (b3 << 16) | (b2 << 8) | b1;
    }
}

package client;

import model.Fruta;
import model.Juego;
import model.Bloque;
import ui.GameWindow;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Cliente que representa a un jugador en el juego iCE Climber.
 *
 * <p>Este cliente se comunica con el servidor para enviar comandos (mover, brincar, golpear),
 * recibir el estado del juego y visualizarlo mediante una interfaz gráfica.</p>
 */
public class PlayerClient extends Client implements IClient {

    /** Nombre del jugador asignado por el servidor ("Popo" o "Nana"). */
    private String nombreJugador;

    /** Indica si la partida es en modo 2 jugadores. */
    private Boolean dosJugadores = false;

    /** Ventana de juego asociada al cliente. */
    private GameWindow window;

    /**
     * Constructor del cliente jugador.
     *
     * @param host dirección IP del servidor
     * @param port puerto de conexión
     * @throws IOException si no se puede establecer la conexión
     */
    public PlayerClient(String host, int port) throws IOException {
        super(host, port);
    }

    /**
     * Se identifica ante el servidor como jugador 1 o 2, según el modo de juego.
     *
     * @return {@code null} si la conexión fue aceptada; lanza excepción si fue rechazada.
     * @throws IOException si no se recibe confirmación o el servidor rechaza la conexión
     */
    @Override
    public String identify() throws IOException {
        if (dosJugadores) {
            out.write("PLAYER2\n".getBytes());
        } else {
            out.write("PLAYER1\n".getBytes());
        }

        out.flush();

        // Leer respuesta tipo "ACCEPTED\n"
        StringBuilder response = new StringBuilder();
        char ch;
        while ((ch = (char) in.readByte()) != '\n') {
            response.append(ch);
        }

        if (!"ACCEPTED".equals(response.toString().trim())) {
            throw new IOException("Conexión rechazada por el servidor");
        }

        // Leer nombre exacto de 10 bytes
        byte[] nombreBytes = new byte[10];
        in.readFully(nombreBytes);
        this.nombreJugador = new String(nombreBytes).trim();
        System.out.println("Jugador asignado: " + nombreJugador);
        return null;
    }

    /**
     * Envía un comando al servidor (por ejemplo, "MOVER:L", "BRINCAR").
     *
     * @param request texto del comando
     * @throws IOException si ocurre un error de escritura
     */
    @Override
    public void sendRequest(String request) throws IOException {
        out.write((request + "\n").getBytes());
        out.flush();
    }

    /**
     * Recibe una respuesta textual del servidor.
     *
     * @return mensaje recibido como cadena
     * @throws IOException si ocurre un error de lectura
     */
    @Override
    public String getResponse() throws IOException {
        byte[] buffer = new byte[1024];
        int bytesRead = in.read(buffer);
        return new String(buffer, 0, bytesRead);
    }

    /**
     * Inicia la escucha de actualizaciones del servidor.
     * Crea la ventana de juego y actualiza el estado del juego y los bloques conforme llegan datos.
     */
    public void startListening() {
        try {
            BufferedWriter output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            SwingUtilities.invokeLater(() -> {
                System.out.println("Creando ventana para " + nombreJugador);
                this.window = new GameWindow(nombreJugador, output, this.dosJugadores);

                new Thread(() -> {
                    try {
                        while (true) {
                            Juego juego = Juego.readFrom(in);
                            window.updateGame(juego);

                            int cantidad = readIntLE(in);
                            if (cantidad < 0 || cantidad > 3001) {
                                throw new IOException("Cantidad inválida de bloques: " + cantidad);
                            }

                            List<Bloque> bloques = new ArrayList<>();
                            for (int i = 0; i < cantidad; i++) {
                                bloques.add(Bloque.readFrom(in));
                            }
                            window.updateBloques(bloques);
                        }
                    } catch (IOException e) {
                        System.err.println("Conexión perdida con el servidor: " + e.getMessage());
                    }
                }).start();
            });

        } catch (IOException e) {
            System.err.println("Error iniciando la ventana del juego: " + e.getMessage());
        }
    }

    /**
     * Devuelve el nombre del jugador asignado por el servidor.
     *
     * @return nombre del jugador ("Popo" o "Nana")
     */
    public String getNombreJugador() {
        return nombreJugador;
    }

    /**
     * Configura si se desea iniciar una partida en modo dos jugadores.
     *
     * @param valor {@code true} para modo 2 jugadores; {@code false} para Popo únicamente
     */
    public void setDosJugadores(boolean valor) {
        this.dosJugadores = valor;
    }

    /**
     * Devuelve la ventana gráfica del juego asociada a este cliente.
     *
     * @return instancia de {@link GameWindow}
     */
    public GameWindow getWindow() {
        return window;
    }

    /**
     * Lee un entero en formato Little Endian desde el flujo de datos.
     *
     * @param in flujo de entrada
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

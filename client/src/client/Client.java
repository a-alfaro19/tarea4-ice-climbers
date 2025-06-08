package client;

import java.io.*;
import java.net.Socket;

/**
 * Clase base abstracta para clientes en el juego iCE Climber.
 * <p>
 * Implementa parcialmente la interfaz {@link IClient}, proporcionando funcionalidades básicas
 * para establecer conexión, enviar solicitudes y recibir respuestas del servidor.
 * </p>
 */
public abstract class Client implements IClient {
    /**
     * Socket asociado a la conexión con el servidor.
     */
    protected Socket socket;

    /**
     * Flujo de salida de datos hacia el servidor.
     */
    protected DataOutputStream out;

    /**
     * Flujo de entrada de datos desde el servidor.
     */
    protected DataInputStream in;

    /**
     * Crea un cliente base y establece conexión con el servidor.
     *
     * @param host dirección IP o nombre del servidor
     * @param port puerto donde el servidor escucha conexiones
     * @throws IOException si ocurre un error al establecer la conexión
     */
    public Client(String host, int port) throws IOException {
        this.socket = new Socket(host, port);
        this.out = new DataOutputStream(socket.getOutputStream());
        this.in = new DataInputStream(socket.getInputStream());
    }

    /**
     * Envía una solicitud textual al servidor.
     *
     * @param request cadena de texto a enviar (por ejemplo: "MOVER:L")
     * @throws IOException si ocurre un error de comunicación
     */
    @Override
    public void sendRequest(String request) throws IOException {
        out.write(request.getBytes());
    }

    /**
     * Recibe una respuesta textual del servidor.
     *
     * @return la respuesta del servidor como cadena
     * @throws IOException si ocurre un error al leer la respuesta
     */
    @Override
    public String getResponse() throws IOException {
        byte[] buffer = new byte[1024];
        int bytesRead = in.read(buffer);
        return new String(buffer, 0, bytesRead);
    }
}

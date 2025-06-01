import java.io.*;
import java.net.Socket;

public abstract class Client implements IClient {
    protected Socket socket; // Client Socket
    protected DataOutputStream out; // Client OutputStream
    protected DataInputStream in; // Client InputStream

    /**
     * Creates a base client
     * @param host Address of the server socket
     * @param port Port where the server is listening
     */
    public Client(String host, int port) throws IOException {
        this.socket = new Socket(host, port);
        this.out = new DataOutputStream(socket.getOutputStream());
        this.in = new DataInputStream(socket.getInputStream());
    }

    @Override
    public void sendRequest(String request) throws IOException {
        out.write(request.getBytes());
    }

    @Override
    public String getResponse() throws IOException {
        byte[] buffer = new byte[1024];
        int bytesRead = in.read(buffer);
        return new String(buffer, 0, bytesRead);
    }
}

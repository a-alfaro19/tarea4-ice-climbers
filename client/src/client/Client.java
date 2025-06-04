package client;

import model.Game;
import ui.Tile;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public abstract class Client implements IClient {
    protected Socket socket; // Client Socket
    protected DataOutputStream out; // Client OutputStream
    protected DataInputStream in; // Client InputStream
    protected Game game; // Game State

    /**
     * Creates a base client
     * @param host Address of the server socket
     * @param port Port where the server is listening
     */
    public Client(String host, int port) throws IOException {
        this.socket = new Socket(host, port);
        this.out = new DataOutputStream(socket.getOutputStream());
        this.in = new DataInputStream(socket.getInputStream());
        this.game = new Game();
    }

    @Override
    public void sendMessage(String request) throws IOException {
        byte[] bytes = request.getBytes(StandardCharsets.UTF_8);

        // Send len first
        System.out.println(bytes.length);
        out.writeInt(bytes.length);

        // Send request
        out.write(bytes);

        out.flush();
    }

    @Override
    public String receiveMessage() throws IOException {
        // Receive len first
        int length = receiveIntLE();

        // Check len
        if (length < 0 || length > 10_000) {
            throw new IOException("Invalid length: " + length);
        }

        // Receive response
        byte[] messageBytes = new byte[length];
        in.readFully(messageBytes);

        return new String(messageBytes, StandardCharsets.UTF_8);
    }

    private int receiveIntLE() throws IOException {
        byte[] bytes = new byte[4];
        in.readFully(bytes);
        return ((bytes[3] & 0xFF) << 24) |
                ((bytes[2] & 0xFF) << 16) |
                ((bytes[1] & 0xFF) << 8)  |
                (bytes[0] & 0xFF);
    }

    @Override
    public Tile[][] receiveMap() throws IOException {
        int rows = receiveIntLE();
        int cols =  receiveIntLE();

        Tile[][] map = new Tile[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int x = receiveIntLE();
                int y = receiveIntLE();
                int type = receiveIntLE();
                map[i][j] = new Tile(x, y, type);
            }
        }

        return map;
    }

    @Override
    public void fetch() throws IOException {
        //Receive Game Data

        // Receive Game Map
        Tile[][] map = receiveMap();
        printMap(map);
        this.game.setMap(map);
    }

    public static void printMap(Tile[][] map) {
        if (map == null) {
            System.out.println("No Map.");
            return;
        }
        System.out.println("Drawing Map");
        for (Tile[] tiles : map) {
            for (Tile tile : tiles) {
                if (tile.type == 0) {
                    System.out.print("N");
                } else if (tile.type == 1) {
                    System.out.print("F");
                } else if (tile.type == 2) {
                    System.out.print("X");
                } else if (tile.type == 3) {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }
}

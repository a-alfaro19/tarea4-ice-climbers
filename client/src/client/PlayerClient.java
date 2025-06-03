package client;

import model.Juego;
import ui.Bloque;
import ui.GamePanel;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerClient extends Client implements IClient {
    private String playerName;

    public PlayerClient(String host, int port) throws IOException {
        super(host, port);
    }

    @Override
    public void identify() throws IOException {
        // Send PLAYER ID
        out.write("PLAYER".getBytes());
        out.flush();

        // Read Server Response
        StringBuilder response = new StringBuilder();
        char ch;
        while ((ch = (char) in.readByte()) != '\n') {
            response.append(ch);
        }

        // Process Response
        if (!"ACCEPTED".equals(response.toString().trim())) {
            throw new IOException("Player Rejected: " + response);
        }

        // Read Player Name assigned by the Server
        byte[] nameBytes = new byte[10];
        in.readFully(nameBytes);
        this.playerName = new String(nameBytes).trim();
        System.out.println("Assigned player: " + playerName);
    }

    @Override
    public void sendRequest(String request) throws IOException {
        out.write((request + "\n").getBytes());
        out.flush();
    }

    @Override
    public String getResponse() throws IOException {
        byte[] buffer = new byte[1024];
        int bytesRead = in.read(buffer);
        return new String(buffer, 0, bytesRead);
    }

    public void start(JFrame mainFrame) {
        try {
            BufferedWriter output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            SwingUtilities.invokeLater(() -> {
                GamePanel gamePanel = new GamePanel(playerName, output);

                // Load Game Data
                gamePanel.updateGame(new Juego());
                gamePanel.updateBloques(new ArrayList<>());

                // Change Panel
                mainFrame.setContentPane(gamePanel);
                mainFrame.revalidate();
                mainFrame.repaint();
                gamePanel.requestFocusInWindow();

                new Thread(() -> {
                    try {
                        while (true) {
                            // Read Game Data from Server
                            Juego juego = Juego.readFrom(in);

                            // Update UI
                            gamePanel.updateGame(juego);

                            // Validate Map
                            int cantidad = readIntLE(in);
                            if (cantidad < 0 || cantidad > 512) {
                                throw new IOException("Error Invalid Block Quantity: " + cantidad);
                            }

                            // Read Map
                            List<Bloque> bloques = new ArrayList<>();
                            for (int i = 0; i < cantidad; i++) {
                                bloques.add(Bloque.readFrom(in));
                            }

                            // Update UI Map
                            gamePanel.updateBloques(bloques);

                        }
                    } catch (IOException e) {
                        System.err.println("Error Server Connection Lost: " + e.getMessage());
                    }
                }).start();
            });

        } catch (IOException e) {
            System.err.println("Error Starting Game Panel: " + e.getMessage());
        }
    }

    private int readIntLE(DataInputStream in) throws IOException {
        int b1 = in.readUnsignedByte();
        int b2 = in.readUnsignedByte();
        int b3 = in.readUnsignedByte();
        int b4 = in.readUnsignedByte();
        return (b4 << 24) | (b3 << 16) | (b2 << 8) | b1;
    }
}


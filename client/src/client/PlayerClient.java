package client;

import model.Game;
import ui.GamePanel;
import ui.Tile;

import javax.swing.*;
import java.io.*;

public class PlayerClient extends Client implements IClient {
    private String playerName;

    public PlayerClient(String host, int port) throws IOException {
        super(host, port);
    }

    @Override
    public void identify() throws IOException {
        // Send PLAYER ID
        sendMessage("PLAYER");

        // Read Server Response
        String response = receiveMessage();

        // Process Response
        if (!"ACCEPTED".equals(response.trim())) {
            throw new IOException("Player Rejected: " + response);
        }

        // Read Player Name assigned by the Server
        this.playerName = receiveMessage();
        System.out.println("Assigned player: " + playerName.trim());
    }

    public void start(JFrame mainFrame) {
        try {
            BufferedWriter output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            SwingUtilities.invokeLater(() -> {
                GamePanel gamePanel = new GamePanel(playerName, output);

                // Load Game Data
                gamePanel.updateGame(new Game());
                gamePanel.updateMap(game.getMap());

                // Change Panel
                mainFrame.setContentPane(gamePanel);
                mainFrame.revalidate();
                mainFrame.repaint();
                gamePanel.requestFocusInWindow();

                new Thread(() -> {
                    try {
                        while (true) {
//                            // Read Game Data from Server
//                            Juego juego = Juego.readFrom(in);
//
//                            // Update UI
//                            gamePanel.updateGame(juego);

                            // Read Map
                            game.setMap(receiveMap());
                            printMap(game.getMap());

                            // Update UI Map
                            gamePanel.updateMap(game.getMap());

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
}


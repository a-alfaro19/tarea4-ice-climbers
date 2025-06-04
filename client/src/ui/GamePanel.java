package ui;

import model.Game;
import model.Jugador;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Objects;
import javax.imageio.ImageIO;

public class GamePanel extends JPanel {
    private final String playerName;
    private final BufferedWriter output;
    private BufferedImage popoImg;
    private BufferedImage nanaImg;
    private BufferedImage liveImg;
    private Jugador[] players;
    private Tile[][] map;

    public GamePanel(String playerName, BufferedWriter output) {
        this.playerName = playerName;
        this.output = output;

        setBackground(Color.BLACK);
        setFocusable(true);
        requestFocusInWindow();

        // Load Game Images
        try {
            popoImg = ImageIO.read(Objects.requireNonNull(getClass().getResource("/ui/figuras/popo.png")));
            nanaImg = ImageIO.read(Objects.requireNonNull(getClass().getResource("/ui/figuras/nana.png")));
            liveImg = ImageIO.read(Objects.requireNonNull(getClass().getResource("/ui/figuras/heart.png")));
        } catch (IOException e) {
            System.err.println("Error loading the image:\n" + e.getMessage());
        }

        // Capture key press
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (players == null) return; // No players

                try {
                    Jugador clientPlayer = null;
                    for (Jugador player : players) {
                        if (player != null && player.nombre.equalsIgnoreCase(playerName)) {
                            clientPlayer = player;
                            break;
                        }
                    }

                    if (clientPlayer == null) return; // Client Player not found on Game

                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_LEFT -> {
                            output.write("MOVE:L\n");
                            output.flush();
                        }

                        case KeyEvent.VK_RIGHT -> {
                            output.write("MOVE:R\n");
                            output.flush();
                        }

                        case KeyEvent.VK_UP -> {
                            output.write("JUMP\n");
                            output.flush();
                        }
//                        case KeyEvent.VK_SPACE -> {
//                            Tile bloqueArriba = obtenerBloqueEn(clientPlayer.x, clientPlayer.y - 1);
//                            if (bloqueArriba != null && bloqueArriba.tipo == 1 && bloqueArriba.activo == 1) {
//                                output.write("GOLPEAR\n");
//                                output.flush();
//                            }
//                        }
                    }

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public void setPlayers(Jugador[] players) {
        this.players = players;
    }

    public void setMap(Tile[][] map) {
        this.map = map;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        final int PANEL_WIDTH = getWidth();
        final int PANEL_HEIGHT = getHeight();

        final int ORIGIN_Y = -2;      // fila inferior visible (donde inicia el jugador)

        final int MAX_LIVES = 3;

        // Draw Players Lives
        int LIVE_ICON_SIZE = 20;
        int NAME_LIVE_SPACING = 5;
        if (liveImg != null && players != null) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 14));

            for (Jugador player: players) {
                if (player == null) continue;

                // Get Player Lives
                int playerLives = player.vidas;

                if ("Popo".equalsIgnoreCase(player.nombre)) { // Draw on left side
                    // Draw Player Name
                    g.drawString("Popo: ", 10, 25);

                    // Draw Player Lives
                    int textWidth = g.getFontMetrics().stringWidth("Popo: ");
                    for (int i = 0; i < playerLives; i++) {
                        int x =  10 + textWidth+ i * (LIVE_ICON_SIZE + NAME_LIVE_SPACING);
                        int y = 10;
                        g.drawImage(liveImg, x, y, LIVE_ICON_SIZE, LIVE_ICON_SIZE, this);
                    }

                } else if ("Nana".equalsIgnoreCase(player.nombre)) {
                    // Draw Player Name
                    String label = "Nana: ";
                    int labelWidth = g.getFontMetrics().stringWidth(label);
                    int totalLivesWidth = MAX_LIVES * (LIVE_ICON_SIZE + NAME_LIVE_SPACING);
                    int totalWidth = labelWidth + NAME_LIVE_SPACING + totalLivesWidth;

                    int xLabel = getWidth() - totalWidth - 10;
                    g.drawString(label, xLabel, 25);

                    // Draw Player Lives
                    for (int i = 0; i < playerLives; i++) {
                        int x = xLabel + labelWidth + NAME_LIVE_SPACING + i * (LIVE_ICON_SIZE + NAME_LIVE_SPACING);
                        int y = 10;
                        g.drawImage(liveImg, x, y, LIVE_ICON_SIZE, LIVE_ICON_SIZE, this);
                    }
                }
            }
        }

        final int MAP_ROWS = map.length;
        final int MAP_COLS = map[0].length;

        final int VISIBLE_ROWS = 19;

        final int TILE_WIDTH = PANEL_WIDTH / MAP_COLS;
        final int TILE_HEIGHT = PANEL_HEIGHT / VISIBLE_ROWS;

        // Draw map
        for (int i = MAP_ROWS - VISIBLE_ROWS, visibleRow = 0;
             i < MAP_ROWS; i++, visibleRow++) {
            for (int j = 0; j < MAP_COLS; j++) {
                Tile tile = map[i][j];

                // Select Tile Color
                g.setColor(switch (tile.type) {
                    case 0, 1 -> Color.CYAN;
                    case 2 -> Color.GRAY;
                    default -> Color.BLACK;
                });

                // Invert Row
                int x = j * TILE_WIDTH;
                int y = PANEL_HEIGHT - (visibleRow + 1) * TILE_HEIGHT;

                // Draw Tile
                g.fillRect(x, y, TILE_WIDTH, TILE_HEIGHT);

                // Draw Shadow
                g.setColor(Color.BLACK);
                g.drawRect(x, y, TILE_WIDTH, TILE_HEIGHT);
            }
        }

        // Draw Players
        if (players != null) {
            for (Jugador player : players) {
                if (player == null) continue;

                int drawX = player.x * TILE_WIDTH;
                int drawY = PANEL_HEIGHT - ((player.y + 1) * TILE_HEIGHT);

                if ("Popo".equalsIgnoreCase(player.nombre) && popoImg != null) {
                    g.drawImage(popoImg, drawX, drawY, TILE_WIDTH, TILE_HEIGHT, this);
                } else if ("Nana".equalsIgnoreCase(player.nombre) && nanaImg != null) {
                    g.drawImage(nanaImg, drawX, drawY, TILE_WIDTH, TILE_HEIGHT, this);
                }
            }
        }
    }

    public void updateGame(Game game) {
        setPlayers(game.players);
        repaint();
    }

    public void updateMap(Tile[][] map) {
        setMap(map);
        repaint();
    }


}

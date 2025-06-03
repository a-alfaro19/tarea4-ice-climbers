package ui;

import model.Juego;
import model.Jugador;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import javax.imageio.ImageIO;

public class GamePanel extends JPanel {
    private final String playerName;
    private final BufferedWriter output;
    private BufferedImage popoImg;
    private BufferedImage nanaImg;
    private BufferedImage liveImg;
    private Jugador[] players;
    private List<Bloque> bloques = new java.util.ArrayList<>();

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
                        case KeyEvent.VK_SPACE -> {
                            Bloque bloqueArriba = obtenerBloqueEn(clientPlayer.x, clientPlayer.y - 1);
                            if (bloqueArriba != null && bloqueArriba.tipo == 1 && bloqueArriba.activo == 1) {
                                output.write("GOLPEAR\n");
                                output.flush();
                            }
                        }
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

    public void setBloques(List<Bloque> bloques) {
        this.bloques = bloques;
    }
    private boolean hayBloqueEn(int x, int y) {
        for (Bloque b : bloques) {
            if (b.activo == 1 && b.x == x && b.y == y) {
                return true;
            }
        }
        return false;
    }

    private Bloque obtenerBloqueEn(int x, int y) {
        for (Bloque b : bloques) {
            if (b.activo == 1 && b.x == x && b.y == y) {
                return b;
            }
        }
        return null;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        final int MAX_LIVES = 3;

        final int TILE_SIZE = 50;
        final int VISIBLE_ROWS = 12;  // cantidad de filas que se pueden ver en pantalla
        final int ORIGIN_Y = -2;      // fila inferior visible (donde inicia el jugador)

        int panelHeight = getHeight();
        int offsetY = panelHeight - TILE_SIZE;

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

        // Draw map
        for (Bloque b : bloques) {
            if (b.activo == 1) {
                g.setColor(switch (b.tipo) {
                    case 1 -> Color.CYAN;
                    case 2 -> Color.GRAY;
                    default -> Color.BLACK;
                });

                int drawX = b.x * TILE_SIZE;
                int drawY = offsetY - (b.y * TILE_SIZE);

                g.fillRect(drawX, drawY, TILE_SIZE, TILE_SIZE);
            }
        }

        // Draw Players
        if (players != null) {
            for (Jugador j : players) {
                if (j == null) continue;

                int drawX = j.x * TILE_SIZE;
                int drawY = offsetY - (j.y * TILE_SIZE);

                if ("Popo".equalsIgnoreCase(j.nombre) && popoImg != null) {
                    g.drawImage(popoImg, drawX, drawY, TILE_SIZE, TILE_SIZE, this);
                } else if ("Nana".equalsIgnoreCase(j.nombre) && nanaImg != null) {
                    g.drawImage(nanaImg, drawX, drawY, TILE_SIZE, TILE_SIZE, this);
                }
            }
        }
    }

    public void updateGame(Juego juego) {
        setPlayers(juego.jugadores); // pasa los jugadores al GamePanel
        repaint();
    }

    public void updateBloques(java.util.List<Bloque> bloques) {
        setBloques(bloques);
        repaint();
    }


}

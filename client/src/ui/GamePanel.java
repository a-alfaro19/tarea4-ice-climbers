package ui;

import model.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GamePanel extends JPanel {
    private final String miNombre;
    private final BufferedWriter output;
    private BufferedImage popoImg;
    private BufferedImage nanaImg;
    private BufferedImage[] frutaImgs = new BufferedImage[4];

    private int nivelActual = 0;
    private final boolean dosJugadores;
    private Jugador[] jugadores;
    private ArrayList<Obstacle> obstacles = new ArrayList<>();
    private List<Fruta> frutas = new ArrayList<>();
    private Tile[][] mapa;
    private final boolean esControlable;

    public GamePanel(String miNombre, BufferedWriter output, boolean dosJugadores, boolean esControlable) {
        this.miNombre = miNombre;
        this.output = output;
        this.dosJugadores = dosJugadores;
        this.esControlable = esControlable;

        setBackground(Color.BLACK);
        setFocusable(true);
        requestFocusInWindow();
        SwingUtilities.invokeLater(this::requestFocusInWindow);

        try {
            popoImg = ImageIO.read(Objects.requireNonNull(getClass().getResource("/ui/figuras/popo.png")));
            nanaImg = ImageIO.read(Objects.requireNonNull(getClass().getResource("/ui/figuras/nana.png")));

            frutaImgs[0] = ImageIO.read(Objects.requireNonNull(getClass().getResource("/ui/figuras/orange.png")));    // Naranja
            frutaImgs[1] = ImageIO.read(Objects.requireNonNull(getClass().getResource("/ui/figuras/banana.png")));    // Banano
            frutaImgs[2] = ImageIO.read(Objects.requireNonNull(getClass().getResource("/ui/figuras/egg_plant.png"))); // Berenjena
            frutaImgs[3] = ImageIO.read(Objects.requireNonNull(getClass().getResource("/ui/figuras/lettuce.png")));   // Lechuga

        } catch (IOException e) {
            System.err.println("Error cargando imágenes: " + e.getMessage());
        }

        // Solo agregar controles si es un jugador
        if (esControlable) {
            addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (jugadores == null || mapa == null) return;

                    try {
                        for (Jugador j : jugadores) {
                            if (j != null && j.nombre.equalsIgnoreCase(miNombre) && j.vidas > 0) {
                                int ancho = mapa[0].length;
                                int alto = mapa.length;

                                switch (e.getKeyCode()) {
                                    case KeyEvent.VK_LEFT -> {
                                        if (j.x > 0) {
                                            output.write("MOVER:L\n");
                                            output.flush();
                                        }
                                    }
                                    case KeyEvent.VK_RIGHT -> {
                                        if (j.x < ancho - 1) {
                                            output.write("MOVER:R\n");
                                            output.flush();
                                        }
                                    }
                                    case KeyEvent.VK_UP -> {
                                        if (j.y < alto - 1) {
                                            output.write("BRINCAR\n");
                                            output.flush();
                                        }
                                    }
                                    case KeyEvent.VK_SPACE -> {
                                        output.write("GOLPEAR\n");
                                        output.flush();
                                    }
                                }
                                break;
                            }
                        }

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });
        }
    }

    public void setNivelActual(int nivelActual) {
        this.nivelActual = nivelActual;
    }

    public void setJugadores(Jugador[] jugadores) {
        this.jugadores = jugadores;
    }
    public void setFrutas(List<Fruta> frutas) {
        this.frutas = frutas;
    }
    public void setObstacles(ArrayList<Obstacle> obstacles) { this.obstacles = obstacles; }

    public void setBloques(List<Bloque> bloques, int ancho, int alto) {
        this.mapa = convertirBloquesAMatriz(bloques, ancho, alto);
    }

    public void updateGame(Jugador[] jugadores, List<Bloque> bloques, int ancho, int alto) {
        setJugadores(jugadores);
        setBloques(bloques, ancho, alto);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (mapa == null) return;

        final int PANEL_WIDTH = getWidth();
        final int PANEL_HEIGHT = getHeight();
        final int ROWS = mapa.length;
        final int COLS = mapa[0].length;
        final int filasPorNivel = 6;
        final int VISIBLE_ROWS = 19;

        int maxY = 0;
        if (jugadores != null) {
            for (Jugador j : jugadores) {
                if (j != null && j.y > maxY) {
                    maxY = j.y;
                }
            }
        }

        final int FIRST_VISIBLE_ROW = Math.max(0, Math.min(maxY - VISIBLE_ROWS / 2, mapa.length - VISIBLE_ROWS));
        final int TILE_WIDTH = PANEL_WIDTH / COLS;
        final int TILE_HEIGHT = PANEL_HEIGHT / VISIBLE_ROWS;

        // Dibujar mapa
        // Dibujar tiles
        for (int i = FIRST_VISIBLE_ROW, visibleRow = 0; i < ROWS && visibleRow < VISIBLE_ROWS; i++, visibleRow++) {
            int nivel = i / filasPorNivel;
            boolean esBonus = nivel >= 9;

            for (int j = 0; j < COLS; j++) {
                Tile tile = mapa[i][j];
                g.setColor(switch (tile.type) {
                    case NORMAL -> esBonus ? new Color(180, 100, 255) : Color.CYAN;
                    case FIXED_TILE -> esBonus ? new Color(120, 48, 191) : Color.GRAY;
                    default -> Color.BLACK;
                });

                int x = j * TILE_WIDTH;
                int y = PANEL_HEIGHT - (visibleRow + 1) * TILE_HEIGHT;
                g.fillRect(x, y, TILE_WIDTH, TILE_HEIGHT);
                g.setColor(Color.BLACK);
                g.drawRect(x, y, TILE_WIDTH, TILE_HEIGHT);
            }

            int filaDentroNivel = i % filasPorNivel;
            if (filaDentroNivel == 4) {
                int y = PANEL_HEIGHT - (visibleRow + 1) * TILE_HEIGHT;
                g.setColor(Color.YELLOW);
                g.setFont(new Font("Monospaced", Font.BOLD, 12));
                String label = (nivel >= 9) ? "Fase Bonus" : "Nivel " + nivel;
                g.drawString(label, PANEL_WIDTH - 100, y + TILE_HEIGHT - 4);
            }
        }

        // Dibujar jugadores
        if (jugadores != null) {
            for (Jugador j : jugadores) {
                if (j == null || j.vidas <= 0) continue;

                // Jugador controlable en modo 1 jugador: ocultar Nana
                if (esControlable && !dosJugadores && j.nombre.equalsIgnoreCase("Nana")) continue;

                // Observador: si no es controlable, y el juego es de 1 jugador, ocultar a Nana
                if (!esControlable && !dosJugadores && j.nombre.equalsIgnoreCase("Nana")) continue;

                int visibleRow = j.y - FIRST_VISIBLE_ROW;
                if (visibleRow < 0 || visibleRow >= VISIBLE_ROWS) continue;

                int drawX = j.x * TILE_WIDTH;
                int drawY = PANEL_HEIGHT - (visibleRow + 1) * TILE_HEIGHT;

                if (j.nombre.equalsIgnoreCase("Popo") && popoImg != null) {
                    g.drawImage(popoImg, drawX, drawY, TILE_WIDTH, TILE_HEIGHT, this);
                } else if (j.nombre.equalsIgnoreCase("Nana") && nanaImg != null) {
                    g.drawImage(nanaImg, drawX, drawY, TILE_WIDTH, TILE_HEIGHT, this);
                }
            }
        }

        // Dibujar vidas
        if (jugadores != null) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 14));

            for (Jugador j : jugadores) {
                if (j == null) continue;

                // Jugador controlable
                if (esControlable) {
                    if (!dosJugadores && j.nombre.equalsIgnoreCase("Nana")) continue;
                }

                // Observador: si es solo Popo, no mostrar a Nana
                if (!esControlable && !dosJugadores && j.nombre.equalsIgnoreCase("Nana")) continue;

                String texto = j.nombre + ": " + j.vidas + " vidas";
                if (j.nombre.equalsIgnoreCase("Popo")) {
                    g.drawString(texto, 10, 25);
                } else if (j.nombre.equalsIgnoreCase("Nana")) {
                    int textWidth = g.getFontMetrics().stringWidth(texto);
                    g.drawString(texto, getWidth() - textWidth - 10, 25);
                }
            }
        }



        // Draw Obstacles
        if (!obstacles.isEmpty()) {
            for (Obstacle obstacle : obstacles) {
                int visibleRow = obstacle.getY() - FIRST_VISIBLE_ROW;
                int drawX = obstacle.getX() * TILE_WIDTH;
                int drawY = PANEL_HEIGHT - (visibleRow + 1) * TILE_HEIGHT;
                g.drawImage(obstacle.getImage(), drawX, drawY, TILE_WIDTH, TILE_HEIGHT, this);
            }
        }
        // Dibujar frutas
        if (frutas != null) {
            for (Fruta fruta : frutas) {
                if (fruta.activa == 0) continue;

                int visibleRow = fruta.y - FIRST_VISIBLE_ROW;
                if (visibleRow < 0 || visibleRow >= VISIBLE_ROWS) continue;

                int drawX = fruta.x * TILE_WIDTH;
                int drawY = PANEL_HEIGHT - (visibleRow + 1) * TILE_HEIGHT;

                if (fruta.tipo >= 0 && fruta.tipo < frutaImgs.length && frutaImgs[fruta.tipo] != null) {
                    g.drawImage(frutaImgs[fruta.tipo], drawX, drawY, TILE_WIDTH, TILE_HEIGHT, this);
                }

            }
        }

    }


    public static Tile[][] convertirBloquesAMatriz(List<Bloque> bloques, int ancho, int alto) {
        Tile[][] mapa = new Tile[alto][ancho];
        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                mapa[y][x] = new Tile(x, y, TileType.EMPTY);
            }
        }

        for (Bloque b : bloques) {
            if (b.activo == 1 && b.y < alto && b.x < ancho) {
                mapa[b.y][b.x] = new Tile(b.x, b.y, TileType.fromInt(b.tipo));
            }
        }

        return mapa;
    }
}
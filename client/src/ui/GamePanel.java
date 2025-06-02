package ui;

import model.Jugador;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;

public class GamePanel extends JPanel {
    private final String miNombre;
    private final BufferedWriter output;
    private BufferedImage popoImg;
    private BufferedImage nanaImg;
    private Jugador[] jugadores;
    private final int TILE_SIZE = 50;
    private List<Bloque> bloques = new java.util.ArrayList<>();

    public GamePanel(String miNombre, BufferedWriter output) {
        this.miNombre = miNombre;
        this.output = output;
        setBackground(Color.BLACK);
        setFocusable(true);
        requestFocusInWindow();

        try {
            popoImg = ImageIO.read(getClass().getResource("/ui/figuras/popo.png"));
            nanaImg = ImageIO.read(getClass().getResource("/ui/figuras/nana.png"));
        } catch (IOException e) {
            System.err.println("No se pudieron cargar imágenes de los personajes.");
        }

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (jugadores == null) return;

                try {
                    Jugador yo = null;
                    for (Jugador j : jugadores) {
                        if (j != null && j.nombre.equalsIgnoreCase(miNombre)) {
                            yo = j;
                            break;
                        }
                    }
                    if (yo == null) return;

                    int nextX = yo.x;
                    int nextY = yo.y;

                    int tilesHorizontales = 20;

                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_LEFT -> {
                            nextX = yo.x - 1;
                            if (nextX < 0) {
                                nextX = tilesHorizontales;
                            }
                            if (!hayBloqueEn(nextX, yo.y)) {
                                output.write("MOVER:L\n");
                                output.flush();
                            }
                        }
                        case KeyEvent.VK_RIGHT -> {
                            nextX = yo.x + 1;
                            if (nextX > tilesHorizontales) {
                                nextX = 0;
                            }
                            if (!hayBloqueEn(nextX, yo.y)) {
                                output.write("MOVER:R\n");
                                output.flush();
                            }
                        }
                        case KeyEvent.VK_UP -> {
                            nextY = yo.y - 1;
                            if (!hayBloqueEn(yo.x, nextY)) {
                                output.write("BRINCAR\n");
                                output.flush();
                            }
                        }
                        case KeyEvent.VK_SPACE -> {
                            Bloque bloqueArriba = obtenerBloqueEn(yo.x, yo.y - 1);
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

    public void setJugadores(Jugador[] jugadores) {
        this.jugadores = jugadores;
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

        final int TILE_SIZE = 50;
        final int VISIBLE_ROWS = 12;  // cantidad de filas que se pueden ver en pantalla
        final int ORIGIN_Y = -2;      // fila inferior visible (donde inicia el jugador)

        int panelHeight = getHeight();
        int offsetY = panelHeight - TILE_SIZE;



        // Dibujar bloques del mapa
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

        // Dibujar jugadores
        if (jugadores != null) {
            for (Jugador j : jugadores) {
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

}

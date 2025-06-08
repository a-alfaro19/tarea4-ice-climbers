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

/**
 * Panel principal del juego iCE Climber.
 * Se encarga de renderizar el estado visual, procesar entradas de teclado,
 * controlar condiciones de victoria y derrota, y mostrar el panel de fin de juego.
 */
public class GamePanel extends JPanel {

    /** Nombre del jugador local (Popo o Nana). */
    private final String miNombre;

    /** Canal de salida hacia el servidor. */
    private final BufferedWriter output;

    /** Imagen del personaje Popo. */
    private BufferedImage popoImg;

    /** Imagen del personaje Nana. */
    private BufferedImage nanaImg;

    /** Imagenes de frutas por tipo (0 = naranja, 1 = banana, etc). */
    private BufferedImage[] frutaImgs = new BufferedImage[4];

    /** Nivel actual del juego. */
    private Integer nivelActual = 0;

    /** Indica si el juego es de dos jugadores. */
    private final Boolean dosJugadores;

    /** Arreglo de jugadores activos (maximo 2). */
    private Jugador[] jugadores;

    /** Lista de obstaculos activos (pajaros, yetis, etc). */
    private ArrayList<Obstacle> obstacles = new ArrayList<>();

    /** Lista de frutas disponibles. */
    private List<Fruta> frutas = new ArrayList<>();

    /** Pterodactilo visible en la fase bonus. */
    private Pterodactilo pterodactilo;

    /** Imagen del pterodactilo. */
    private BufferedImage pteroImg;

    /** Mapa del nivel en forma de matriz de Tiles. */
    private Tile[][] mapa;

    /** True si este panel puede recibir entradas del teclado. */
    private final Boolean esControlable;

    /** Bandera para detectar si un jugador atrapo al pterodactilo. */
    private Integer atrapo_ptero = 0;

    /** True si el juego ha terminado. */
    private Boolean gameOver = false;

    /** Referencia a la ventana principal del juego (opcional). */
    private GameWindow gameWindow;

    /**
     * Crea un nuevo GamePanel que representa el estado visual del juego.
     *
     * @param miNombre nombre del jugador local (Popo o Nana)
     * @param output flujo de salida al servidor
     * @param dosJugadores indica si la partida es de dos jugadores
     * @param esControlable indica si este cliente controla un jugador
     */
    public GamePanel(String miNombre, BufferedWriter output, Boolean dosJugadores, Boolean esControlable) {
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

            frutaImgs[0] = ImageIO.read(Objects.requireNonNull(getClass().getResource("/ui/figuras/orange.png")));
            frutaImgs[1] = ImageIO.read(Objects.requireNonNull(getClass().getResource("/ui/figuras/banana.png")));
            frutaImgs[2] = ImageIO.read(Objects.requireNonNull(getClass().getResource("/ui/figuras/egg_plant.png")));
            frutaImgs[3] = ImageIO.read(Objects.requireNonNull(getClass().getResource("/ui/figuras/lettuce.png")));

            pteroImg = ImageIO.read(Objects.requireNonNull(getClass().getResource("/ui/figuras/pterodactilo.png")));

        } catch (IOException e) {
            System.err.println("Error cargando imagenes: " + e.getMessage());
        }

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

    /** Asigna el nivel actual. */
    public void setNivelActual(Integer nivelActual) {
        this.nivelActual = nivelActual;
    }

    /** Asigna los jugadores actuales. */
    public void setJugadores(Jugador[] jugadores) {
        this.jugadores = jugadores;
    }

    /** Asigna la lista de frutas. */
    public void setFrutas(List<Fruta> frutas) {
        this.frutas = frutas;
    }

    /** Asigna la lista de obstaculos. */
    public void setObstacles(ArrayList<Obstacle> obstacles) {
        this.obstacles = obstacles;
    }

    /**
     * Asigna los bloques y genera el mapa visual.
     *
     * @param bloques lista de bloques
     * @param ancho cantidad de columnas
     * @param alto cantidad de filas
     */
    public void setBloques(List<Bloque> bloques, int ancho, int alto) {
        this.mapa = convertirBloquesAMatriz(bloques, ancho, alto);
    }

    /** Asigna el pterodactilo. */
    public void setPterodactilo(Pterodactilo pterodactilo) {
        this.pterodactilo = pterodactilo;
    }

    /** Indica si se ha atrapado al pterodactilo. */
    public void setAtrapoPtero(int valor) {
        this.atrapo_ptero = valor;
    }

    /**
     * Actualiza el estado del juego con los jugadores y bloques.
     *
     * @param jugadores arreglo de jugadores
     * @param bloques bloques del mapa
     * @param ancho cantidad de columnas
     * @param alto cantidad de filas
     */
    public void updateGame(Jugador[] jugadores, List<Bloque> bloques, int ancho, int alto) {
        setJugadores(jugadores);
        setBloques(bloques, ancho, alto);
        repaint();
    }

    /**
     * Dibuja el mapa, personajes, frutas, pterodactilo y obstaculos.
     *
     * @param g contexto grafico
     */
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

        // Jugadores
        if (jugadores != null) {
            for (Jugador j : jugadores) {
                if (j == null || j.vidas <= 0) continue;
                if (!dosJugadores && j.nombre.equalsIgnoreCase("Nana")) continue;
                int visibleRow = j.y - FIRST_VISIBLE_ROW;
                if (visibleRow < 0 || visibleRow >= VISIBLE_ROWS) continue;
                int drawX = j.x * TILE_WIDTH;
                int drawY = PANEL_HEIGHT - (visibleRow + 1) * TILE_HEIGHT;
                BufferedImage img = j.nombre.equalsIgnoreCase("Popo") ? popoImg : nanaImg;
                if (img != null) g.drawImage(img, drawX, drawY, TILE_WIDTH, TILE_HEIGHT, this);
            }

            // Vidas
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 14));
            for (Jugador j : jugadores) {
                if (j == null) continue;
                if (!dosJugadores && j.nombre.equalsIgnoreCase("Nana")) continue;
                String texto = j.nombre + ": " + j.vidas + " vidas";
                int x = j.nombre.equalsIgnoreCase("Popo") ? 10 : getWidth() - g.getFontMetrics().stringWidth(texto) - 10;
                g.drawString(texto, x, 25);
            }
        }

        // Obstaculos
        for (Obstacle o : obstacles) {
            int row = o.getY() - FIRST_VISIBLE_ROW;
            int drawX = o.getX() * TILE_WIDTH;
            int drawY = PANEL_HEIGHT - (row + 1) * TILE_HEIGHT;
            g.drawImage(o.getImage(), drawX, drawY, TILE_WIDTH, TILE_HEIGHT, this);
        }

        // Frutas
        for (Fruta f : frutas) {
            if (f.activa == 0) continue;
            int row = f.y - FIRST_VISIBLE_ROW;
            if (row < 0 || row >= VISIBLE_ROWS) continue;
            int x = f.x * TILE_WIDTH;
            int y = PANEL_HEIGHT - (row + 1) * TILE_HEIGHT;
            if (f.tipo >= 0 && f.tipo < frutaImgs.length && frutaImgs[f.tipo] != null) {
                g.drawImage(frutaImgs[f.tipo], x, y, TILE_WIDTH, TILE_HEIGHT, this);
            }
        }

        // Pterodactilo
        if (pterodactilo != null && pterodactilo.activo == 1 && pteroImg != null) {
            int row = pterodactilo.y - FIRST_VISIBLE_ROW;
            if (row >= 0 && row < VISIBLE_ROWS) {
                int x = pterodactilo.x * TILE_WIDTH;
                int y = PANEL_HEIGHT - (row + 1) * TILE_HEIGHT;
                Graphics2D g2d = (Graphics2D) g.create();
                if (pterodactilo.direccion == -1) {
                    g2d.translate(x + TILE_WIDTH, y);
                    g2d.scale(-1, 1);
                    g2d.drawImage(pteroImg, 0, 0, TILE_WIDTH, TILE_HEIGHT, this);
                } else {
                    g2d.drawImage(pteroImg, x, y, TILE_WIDTH, TILE_HEIGHT, this);
                }
                g2d.dispose();
            }
        }
    }

    /**
     * Convierte una lista de bloques en matriz de tiles para renderizar.
     *
     * @param bloques lista de bloques
     * @param ancho cantidad de columnas
     * @param alto cantidad de filas
     * @return matriz de tiles
     */
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
    /**
     * Verifica si todos los jugadores han perdido sus vidas.
     * <p>
     * En partidas de un solo jugador, termina el juego si ese jugador pierde todas sus vidas.
     * En partidas de dos jugadores, el juego termina solo si ambos han perdido todas sus vidas.
     */
    public void verificarSiTodosMuertos() {
        if (jugadores == null || jugadores.length == 0) {
            System.out.println("Lista de jugadores vacía o nula");
            return;
        }

        Jugador popo = obtenerJugadorPorNombre("Popo");
        Jugador nana = obtenerJugadorPorNombre("Nana");

        if (esPartidaUnJugador()) {
            verificarJugadorUnicoMuerto(popo);
        }

        if (esPartidaDosJugadores()) {
            verificarAmbosJugadoresMuertos(popo, nana);
        }
    }

    /**
     * Verifica si algún jugador atrapó al pterodáctilo.
     * Si se detecta el valor correspondiente, se termina el juego.
     */
    public void verificarAtrapoPtero() {
        if (!gameOver && atrapo_ptero.equals(1)) {
            terminarJuego("¡Un jugador atrapó al pterodáctilo!");
        }
    }

    /**
     * Determina si la partida es de un solo jugador.
     *
     * @return true si no es de dos jugadores.
     */
    private Boolean esPartidaUnJugador() {
        return !dosJugadores;
    }

    /**
     * Determina si la partida es de dos jugadores.
     *
     * @return true si participan dos jugadores.
     */
    public Boolean esPartidaDosJugadores() {
        return dosJugadores;
    }

    /**
     * Busca un jugador por su nombre en el arreglo actual.
     *
     * @param nombre nombre del jugador a buscar
     * @return instancia del jugador si se encuentra, null si no
     */
    private Jugador obtenerJugadorPorNombre(String nombre) {
        for (Jugador j : jugadores) {
            if (j != null && j.nombre.equalsIgnoreCase(nombre)) {
                return j;
            }
        }
        return null;
    }

    /**
     * Verifica si el único jugador activo ha perdido todas sus vidas.
     * Si es así, termina la partida.
     *
     * @param jugador el único jugador en juego
     */
    private void verificarJugadorUnicoMuerto(Jugador jugador) {
        if (jugador != null && jugador.vidas == 0 && !gameOver) {
            terminarJuego("Solo " + jugador.nombre + " está jugando y ha perdido todas sus vidas.");
        }
    }

    /**
     * Verifica si ambos jugadores han perdido sus vidas.
     * Si es así, termina la partida.
     *
     * @param j1 jugador 1 (Popo)
     * @param j2 jugador 2 (Nana)
     */
    private void verificarAmbosJugadoresMuertos(Jugador j1, Jugador j2) {
        if (j1 != null && j2 != null && j1.vidas == 0 && j2.vidas == 0 && !gameOver) {
            terminarJuego(j1.nombre + " y " + j2.nombre + " han perdido todas sus vidas.");
        }
    }

    /**
     * Termina la partida y muestra el panel de fin de juego.
     * Se envía el comando "GAME_OVER" al servidor.
     *
     * @param mensaje mensaje que se mostrará en consola como causa del fin del juego
     */
    private void terminarJuego(String mensaje) {
        gameOver = true;
        System.out.println("Fin del juego: " + mensaje);

        if (output != null) {
            try {
                output.write("GAME_OVER\n");
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Output es null");
        }

        SwingUtilities.invokeLater(() -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            if (frame != null) {
                frame.getContentPane().removeAll();
                frame.setContentPane(new GameOverPanel(jugadores, frutas, obstacles, pterodactilo, dosJugadores, output));
                frame.revalidate();
                frame.repaint();
            }
        });
    }

}

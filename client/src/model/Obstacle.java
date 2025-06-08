package model;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;

/**
 * Representa un obstáculo en el juego iCE Climber, como un Yeti, un ave o un bloque de hielo.
 *
 * <p>Cada obstáculo tiene una posición (x, y) y una imagen que lo representa visualmente.</p>
 */
public class Obstacle {

    /** Coordenada horizontal del obstáculo (columna). */
    protected Integer x;

    /** Coordenada vertical del obstáculo (fila). */
    protected Integer y;

    /** Imagen asociada al obstáculo. */
    protected BufferedImage image;

    /**
     * Constructor del obstáculo, inicializa su tipo y posición.
     *
     * @param type tipo del obstáculo (ver {@link ObstacleType})
     * @param x posición horizontal
     * @param y posición vertical
     * @throws IOException si ocurre un error al cargar la imagen
     */
    public Obstacle(ObstacleType type, Integer x, Integer y) throws IOException {
        // Ruta del archivo de imagen según tipo de obstáculo
        String filePath = switch (type) {
            case YETI -> "src/ui/figuras/yeti.png";
            case BIRD -> "src/ui/figuras/aveA.png";
            case ICE_BLOCK -> "src/ui/figuras/hielo.png";
        };

        try {
            image = ImageIO.read(new File(filePath));
        } catch (IOException e) {
            System.err.println("Error loading Obstacle Image: " + e.getMessage());
        }

        this.x = x;
        this.y = y;
    }

    /**
     * Devuelve la posición X del obstáculo.
     *
     * @return coordenada X
     */
    public Integer getX() {
        return this.x;
    }

    /**
     * Devuelve la posición Y del obstáculo.
     *
     * @return coordenada Y
     */
    public Integer getY() {
        return this.y;
    }

    /**
     * Devuelve la imagen del obstáculo que será renderizada en pantalla.
     *
     * @return imagen del obstáculo
     */
    public BufferedImage getImage() {
        return this.image;
    }

    /**
     * Crea una instancia de {@code Obstacle} leyendo sus datos desde un flujo binario.
     *
     * @param in flujo de entrada desde el cual se leen los datos del obstáculo
     * @return objeto {@code Obstacle} reconstruido desde los datos binarios
     * @throws IOException si ocurre un error durante la lectura
     */
    public static Obstacle readFrom(DataInputStream in) throws IOException {
        Integer type = readIntLE(in);
        Integer x = readIntLE(in);
        Integer y = readIntLE(in);

        return new Obstacle(ObstacleType.values()[type], x, y);
    }

    /**
     * Lee un entero en formato Little Endian desde el flujo de datos.
     *
     * @param in flujo de entrada
     * @return entero reconstruido desde los bytes
     * @throws IOException si ocurre un error durante la lectura
     */
    private static Integer readIntLE(DataInputStream in) throws IOException {
        int b1 = in.readUnsignedByte();
        int b2 = in.readUnsignedByte();
        int b3 = in.readUnsignedByte();
        int b4 = in.readUnsignedByte();
        return (b4 << 24) | (b3 << 16) | (b2 << 8) | b1;
    }
}

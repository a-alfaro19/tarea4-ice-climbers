import ui.GameWindow;
import ui.MainMenuPanel;

import javax.swing.*;
/**
 * Clase principal que inicia la aplicación iCE Climber.
 * Configura la ventana principal e inserta el menú inicial.
 * <p>
 * La ejecución del programa comienza aquí. Se utiliza Swing para manejar la interfaz gráfica.
 */
public class Main {
    public static void main(String[] args) {
        /**
         * Metodo principal. Inicializa la ventana principal y carga el menú.
         *
         * @param args Argumentos de línea de comandos (no utilizados).
         */
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("iCE Climber - Menú Principal");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(900, 700);
            frame.setResizable(false);
            frame.setLocationRelativeTo(null);

            MainMenuPanel menuPanel = new MainMenuPanel(frame);
            frame.setContentPane(menuPanel);
            frame.setVisible(true);
        });
    }
}


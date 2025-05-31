import ui.GameWindow;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // 1. Mostrar ventana primero en el hilo de eventos de Swing
        //SwingUtilities.invokeLater(() -> new GameWindow());

        // 2. Ejecutar cliente en otro hilo para no bloquear la GUI
        new Thread(() -> {
            IClient client = ClientFactory.createClient("default", "localhost", 8080);
            client.start();
        }).start();
    }
}
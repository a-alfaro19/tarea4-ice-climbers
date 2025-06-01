import ui.GameWindow;

import javax.swing.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        // 1. Mostrar ventana primero en el hilo de eventos de Swing
        SwingUtilities.invokeLater(() -> new GameWindow());

        // 2. Ejecutar cliente en otro hilo para no bloquear la GUI
        new Thread(() -> {
            try {
                IClient playerClient = ClientFactory.createClient("OBSERVER", "localhost", 8080);
                playerClient.identify();
                playerClient.sendRequest("STATE");
                String response = playerClient.getResponse();
                System.out.println(response);
            } catch (IOException e) {
                System.err.println("Connection Error: " + e.getMessage());
            }
        }).start();
    }
}
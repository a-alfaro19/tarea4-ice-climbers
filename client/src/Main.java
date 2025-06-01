import ui.GameWindow;

import javax.swing.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Thread clientThread = new Thread(() -> {
            try {
                PlayerClient client = (PlayerClient) ClientFactory.createClient("PLAYER", "localhost", 8080);
                client.identify();
                client.startListening();
            } catch (IOException e) {
                System.err.println("Error al iniciar el cliente: " + e.getMessage());
            }
        });

        clientThread.start();

        try {
            clientThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


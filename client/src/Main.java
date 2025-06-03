import ui.GameWindow;
import ui.MainMenuPanel;

import javax.swing.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("ICE CLIMBERS - MENU");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1280, 720);
            frame.setLocationRelativeTo(null);
            frame.setResizable(false);

            frame.setContentPane(new MainMenuPanel());
            frame.setVisible(true);
        });
//        Thread clientThread = new Thread(() -> {
//            try {
//                client.PlayerClient client = (client.PlayerClient) client.ClientFactory.createClient("PLAYER", "localhost", 8080);
//                client.identify();
//                client.startListening();
//            } catch (IOException e) {
//                System.err.println("Error al iniciar el cliente: " + e.getMessage());
//            }
//        });
//
//        clientThread.start();
//
//        try {
//            clientThread.join();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }
}


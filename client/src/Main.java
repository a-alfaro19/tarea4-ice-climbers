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

            MainMenuPanel menuPanel = new MainMenuPanel(frame);
            frame.setContentPane(menuPanel);
            frame.setVisible(true);
        });
    }
}


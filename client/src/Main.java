import ui.GameWindow;
import ui.MainMenuPanel;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
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


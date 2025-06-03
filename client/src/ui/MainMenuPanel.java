package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import client.*;

public class MainMenuPanel extends JPanel {
    private final String[] options = {"PLAY", "OBSERVE", "EXIT"}; // Menu options
    private int selection = 0; // Selected option index

    public MainMenuPanel() {
        setBackground(Color.BLACK);
        setFocusable(true);
        requestFocusInWindow();

        // Capture key press
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP -> {
                        selection = (selection - 1 + options.length) % options.length;
                        repaint();
                    }

                    case KeyEvent.VK_DOWN -> {
                        selection = (selection + 1) % options.length;
                        repaint();
                    }

                    case KeyEvent.VK_ENTER -> executeOption();
                }
            }
        });
    }

    /**
     * @brief Executes the action associated with the currently selected option.
     */
    private void executeOption() {
        String selectedOption = options[selection];
        switch (selectedOption) {
            case "PLAY" -> {
                System.out.println("Initializing Game...");

                Thread clientThread = new Thread(() -> {
                    try {
                        // Start Player Client
                        PlayerClient client = (PlayerClient) ClientFactory.createClient("PLAYER", "localhost", 8080);
                        client.identify();
                        client.startListening();

                    } catch (IOException e) {
                        // Show Error Message
                        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(
                                this,
                                "Error starting client:\n" + e.getMessage(),
                                "Connection Error",
                                JOptionPane.ERROR_MESSAGE
                        ));
                        System.err.println("Error starting client: " + e.getMessage());
                    }
                });

                clientThread.start();
            }
            case "OBSERVE" -> System.out.println("Observer Mode...");
            case "EXIT" -> System.exit(0);
        }
    }

    /**
     * @brief Draws the Main Menu Panel
     * @param g the <code>Graphics</code> object to protect
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Window Dimensions
        int width = getWidth();
        int height = getHeight();

        // Draw title
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 64));
        String title = "ICE CLIMBER";
        int titleWidth = g.getFontMetrics().stringWidth(title);
        g.drawString(title,(width - titleWidth) / 2, height / 4);

        // Draw options
        g.setFont(new Font("Arial", Font.PLAIN, 36));
        int optionHeight = 50;
        int startY = height / 2 - (options.length * optionHeight) / 2;

        for (int i = 0; i < options.length; i++) {
            String text = (i == selection ? "> " : "   ") + options[i];
            g.setColor(i == selection ? Color.YELLOW : Color.WHITE);
            int textWidth = g.getFontMetrics().stringWidth(text);
            g.drawString(text, (width - textWidth) / 2, startY + i * optionHeight);
        }
    }
}

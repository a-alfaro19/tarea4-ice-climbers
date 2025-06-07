package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class GameOverWindow extends JFrame {
    public GameOverWindow(boolean dosJugadores) {
        setTitle("¡Game Over!");
        setSize(800, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel leftPanel = new JPanel(null);
        leftPanel.setBackground(Color.BLACK);

        // Botones "Reiniciar" y "Salir"
        String[] opciones = { "Reiniciar", "Salir" };
        JButton[] botones = new JButton[opciones.length];
        int[] selectedIndex = { 0 }; // Se usa array para permitir modificación dentro del KeyListener

        for (int i = 0; i < opciones.length; i++) {
            botones[i] = new JButton(opciones[i]);
            botones[i].setFocusable(false);
            botones[i].setFont(new Font("Monospaced", Font.PLAIN, 24));
            botones[i].setBounds(230, 520 + i * 50, 300, 100);
            botones[i].setContentAreaFilled(false); // Quitar fondo
            botones[i].setBorderPainted(false);     // Quitar borde
            botones[i].setFocusPainted(false);      // Quitar resaltado de foco
            botones[i].setForeground(Color.WHITE);  // Color por defecto
            leftPanel.add(botones[i]);
        }

// Método para actualizar selección visual
        Runnable actualizarSeleccion = () -> {
            for (int i = 0; i < botones.length; i++) {
                if (i == selectedIndex[0]) {
                    botones[i].setFont(new Font("Monospaced", Font.BOLD, 26)); // más grande y en negrita
                    botones[i].setForeground(Color.YELLOW); // resaltar el texto seleccionado
                    botones[i].setText("> " + opciones[i]); // Añadir indicador
                } else {
                    botones[i].setFont(new Font("Monospaced", Font.PLAIN, 24));
                    botones[i].setForeground(Color.WHITE);
                    botones[i].setText("  " + opciones[i]); // Alinear con espacio
                }
            }
        };
        actualizarSeleccion.run();

// Key bindings en el panel para navegación
        leftPanel.setFocusable(true);
        leftPanel.requestFocusInWindow();

        leftPanel.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_UP) {
                    selectedIndex[0] = (selectedIndex[0] - 1 + botones.length) % botones.length;
                    actualizarSeleccion.run();
                } else if (key == KeyEvent.VK_DOWN) {
                    selectedIndex[0] = (selectedIndex[0] + 1) % botones.length;
                    actualizarSeleccion.run();
                } else if (key == KeyEvent.VK_ENTER) {
                    if (selectedIndex[0] == 0) {
                        // Acción de reiniciar
                        System.out.println("Reiniciar el juego...");

                        dispose(); // Cierra esta ventana
                        // TODO: invocar el reinicio del juego aquí
                    } else if (selectedIndex[0] == 1) {
                        System.out.println("Salir del juego...");
                        System.exit(0);
                    }
                }
            }
        });

        // Etiqueta grande: "El juego ha finalizado"
        JLabel gameOverTitle = new JLabel("El juego ha finalizado", SwingConstants.CENTER);
        gameOverTitle.setForeground(Color.WHITE);
        gameOverTitle.setFont(new Font("Monospaced", Font.BOLD, 36));
        gameOverTitle.setBounds(0, 50, 800, 50);
        leftPanel.add(gameOverTitle);

        try {
            BufferedImage aveV = ImageIO.read(getClass().getResource("/ui/figuras/aveN.png"));
            BufferedImage hieloV = ImageIO.read(getClass().getResource("/ui/figuras/hielo.png"));
            BufferedImage yetiV = ImageIO.read(getClass().getResource("/ui/figuras/yeti.png"));
            BufferedImage popoV = ImageIO.read(getClass().getResource("/ui/figuras/popo.png"));
            BufferedImage nanaV = ImageIO.read(getClass().getResource("/ui/figuras/nana.png"));

            // Jugador 1
            JLabel itemLabel = new JLabel(new ImageIcon(hieloV.getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
            itemLabel.setBounds(170, 220, 50, 50);
            JLabel itemLabel3 = new JLabel(new ImageIcon(aveV.getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
            itemLabel3.setBounds(170, 270, 50, 50);
            JLabel itemLabel5 = new JLabel(new ImageIcon(yetiV.getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
            itemLabel5.setBounds(170, 320, 50, 50);
            JLabel itemLabel7 = new JLabel(new ImageIcon(popoV.getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
            itemLabel7.setBounds(170, 420, 50, 50);
            JLabel textLabel = new JLabel("JUGADOR 1");
            textLabel.setForeground(Color.WHITE);
            textLabel.setFont(new Font("Monospaced", Font.BOLD, 20));
            textLabel.setBounds(197, 190, 200, 20);
            JLabel textLabel3 = new JLabel("10 X 0");
            textLabel3.setForeground(Color.WHITE);
            textLabel3.setFont(new Font("Monospaced", Font.BOLD, 20));
            textLabel3.setBounds(230, 220, 200, 20);
            JLabel textLabel5 = new JLabel("800 X 0");
            textLabel5.setForeground(Color.WHITE);
            textLabel5.setFont(new Font("Monospaced", Font.BOLD, 20));
            textLabel5.setBounds(230, 270, 200, 20);
            JLabel textLabel7 = new JLabel("400 X 0");
            textLabel7.setForeground(Color.WHITE);
            textLabel7.setFont(new Font("Monospaced", Font.BOLD, 20));
            textLabel7.setBounds(230, 320, 200, 20);
            JLabel textLabel9 = new JLabel("TOTAL: ");
            textLabel9.setForeground(Color.WHITE);
            textLabel9.setFont(new Font("Monospaced", Font.BOLD, 20));
            textLabel9.setBounds(190, 370, 200, 20);
            JLabel textLabel11 = new JLabel("BONUS: NO!");
            textLabel11.setForeground(Color.WHITE);
            textLabel11.setFont(new Font("Monospaced", Font.BOLD, 18));
            textLabel11.setBounds(220, 420, 200, 20);

            leftPanel.add(itemLabel);
            leftPanel.add(itemLabel3);
            leftPanel.add(itemLabel5);
            leftPanel.add(itemLabel7);
            leftPanel.add(textLabel);
            leftPanel.add(textLabel3);
            leftPanel.add(textLabel5);
            leftPanel.add(textLabel7);
            leftPanel.add(textLabel9);
            leftPanel.add(textLabel11);

            // Solo agregar Jugador 2 si es una partida de dos jugadores
            if (dosJugadores) {
                JLabel itemLabel2 = new JLabel(new ImageIcon(hieloV.getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
                itemLabel2.setBounds(470, 220, 50, 50);
                JLabel itemLabel4 = new JLabel(new ImageIcon(aveV.getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
                itemLabel4.setBounds(470, 270, 50, 50);
                JLabel itemLabel6 = new JLabel(new ImageIcon(yetiV.getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
                itemLabel6.setBounds(470, 320, 50, 50);
                JLabel itemLabel8 = new JLabel(new ImageIcon(nanaV.getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
                itemLabel8.setBounds(470, 420, 50, 50);
                JLabel textLabel2 = new JLabel("JUGADOR 2");
                textLabel2.setForeground(Color.WHITE);
                textLabel2.setFont(new Font("Monospaced", Font.BOLD, 20));
                textLabel2.setBounds(497, 190, 200, 20);
                JLabel textLabel4 = new JLabel("10 X 0");
                textLabel4.setForeground(Color.WHITE);
                textLabel4.setFont(new Font("Monospaced", Font.BOLD, 20));
                textLabel4.setBounds(530, 220, 200, 20);
                JLabel textLabel6 = new JLabel("800 X 0");
                textLabel6.setForeground(Color.WHITE);
                textLabel6.setFont(new Font("Monospaced", Font.BOLD, 20));
                textLabel6.setBounds(530, 270, 200, 20);
                JLabel textLabel8 = new JLabel("400 X 0");
                textLabel8.setForeground(Color.WHITE);
                textLabel8.setFont(new Font("Monospaced", Font.BOLD, 20));
                textLabel8.setBounds(530, 320, 200, 20);
                JLabel textLabel10 = new JLabel("TOTAL: ");
                textLabel10.setForeground(Color.WHITE);
                textLabel10.setFont(new Font("Monospaced", Font.BOLD, 20));
                textLabel10.setBounds(490, 370, 200, 20);
                JLabel textLabel12 = new JLabel("BONUS: NO!");
                textLabel12.setForeground(Color.WHITE);
                textLabel12.setFont(new Font("Monospaced", Font.BOLD, 18));
                textLabel12.setBounds(520, 420, 200, 20);

                leftPanel.add(itemLabel2);
                leftPanel.add(itemLabel4);
                leftPanel.add(itemLabel6);
                leftPanel.add(itemLabel8);
                leftPanel.add(textLabel2);
                leftPanel.add(textLabel4);
                leftPanel.add(textLabel6);
                leftPanel.add(textLabel8);
                leftPanel.add(textLabel10);
                leftPanel.add(textLabel12);
            }

        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Error al cargar imágenes del panel Game Over: " + e.getMessage());
        }

        add(leftPanel);
    }
}

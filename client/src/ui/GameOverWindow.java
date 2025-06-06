package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class GameOverWindow extends JFrame {
    public GameOverWindow() {
        setTitle("¡Game Over!");
        setSize(300, 700);
        setLocationRelativeTo(null); // Centra la ventana en pantalla
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel leftPanel = new JPanel(null);  // Layout absoluto
        leftPanel.setBackground(Color.BLACK);

        try {
            BufferedImage aveV = ImageIO.read(getClass().getResource("/ui/figuras/aveN.png"));
            BufferedImage hieloV = ImageIO.read(getClass().getResource("/ui/figuras/hielo.png"));
            BufferedImage yetiV = ImageIO.read(getClass().getResource("/ui/figuras/yeti.png"));
            BufferedImage popoV = ImageIO.read(getClass().getResource("/ui/figuras/popo.png"));
            BufferedImage nanaV = ImageIO.read(getClass().getResource("/ui/figuras/nana.png"));

            JLabel itemLabel = new JLabel(new ImageIcon(hieloV.getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
            itemLabel.setBounds(20, 70, 50, 50);
            JLabel itemLabel2 = new JLabel(new ImageIcon(hieloV.getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
            itemLabel2.setBounds(20, 410, 50, 50);
            JLabel itemLabel3 = new JLabel(new ImageIcon(aveV.getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
            itemLabel3.setBounds(20, 120, 50, 50);
            JLabel itemLabel4 = new JLabel(new ImageIcon(aveV.getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
            itemLabel4.setBounds(20, 460, 50, 50);
            JLabel itemLabel5 = new JLabel(new ImageIcon(yetiV.getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
            itemLabel5.setBounds(20, 170, 50, 50);
            JLabel itemLabel6 = new JLabel(new ImageIcon(yetiV.getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
            itemLabel6.setBounds(20, 510, 50, 50);
            JLabel itemLabel7 = new JLabel(new ImageIcon(popoV.getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
            itemLabel7.setBounds(20, 270, 50, 50);
            JLabel itemLabel8 = new JLabel(new ImageIcon(nanaV.getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
            itemLabel8.setBounds(20, 610, 50, 50);

            JLabel textLabel = new JLabel("JUGADOR 1");
            textLabel.setForeground(Color.WHITE);
            textLabel.setFont(new Font("Monospaced", Font.BOLD, 20));
            textLabel.setBounds(47, 40, 200, 20);
            JLabel textLabel2 = new JLabel("JUGADOR 2");
            textLabel2.setForeground(Color.WHITE);
            textLabel2.setFont(new Font("Monospaced", Font.BOLD, 20));
            textLabel2.setBounds(47, 380, 200, 20);
            JLabel textLabel3 = new JLabel("10 X 0");
            textLabel3.setForeground(Color.WHITE);
            textLabel3.setFont(new Font("Monospaced", Font.BOLD, 20));
            textLabel3.setBounds(80, 70, 200, 20);
            JLabel textLabel4 = new JLabel("10 X 0");
            textLabel4.setForeground(Color.WHITE);
            textLabel4.setFont(new Font("Monospaced", Font.BOLD, 20));
            textLabel4.setBounds(80, 410, 200, 20);
            JLabel textLabel5 = new JLabel("800 X 0");
            textLabel5.setForeground(Color.WHITE);
            textLabel5.setFont(new Font("Monospaced", Font.BOLD, 20));
            textLabel5.setBounds(80, 120, 200, 20);
            JLabel textLabel6 = new JLabel("800 X 0");
            textLabel6.setForeground(Color.WHITE);
            textLabel6.setFont(new Font("Monospaced", Font.BOLD, 20));
            textLabel6.setBounds(80, 460, 200, 20);
            JLabel textLabel7 = new JLabel("400 X 0");
            textLabel7.setForeground(Color.WHITE);
            textLabel7.setFont(new Font("Monospaced", Font.BOLD, 20));
            textLabel7.setBounds(80, 170, 200, 20);
            JLabel textLabel8 = new JLabel("400 X 0");
            textLabel8.setForeground(Color.WHITE);
            textLabel8.setFont(new Font("Monospaced", Font.BOLD, 20));
            textLabel8.setBounds(80, 510, 200, 20);
            JLabel textLabel9 = new JLabel("TOTAL: ");
            textLabel9.setForeground(Color.WHITE);
            textLabel9.setFont(new Font("Monospaced", Font.BOLD, 20));
            textLabel9.setBounds(40, 220, 200, 20);
            JLabel textLabel10 = new JLabel("TOTAL: ");
            textLabel10.setForeground(Color.WHITE);
            textLabel10.setFont(new Font("Monospaced", Font.BOLD, 20));
            textLabel10.setBounds(40, 560, 200, 20);
            JLabel textLabel11 = new JLabel("BONUS: NO!");
            textLabel11.setForeground(Color.WHITE);
            textLabel11.setFont(new Font("Monospaced", Font.BOLD, 18));
            textLabel11.setBounds(70, 270, 200, 20);
            JLabel textLabel12 = new JLabel("BONUS: NO!");
            textLabel12.setForeground(Color.WHITE);
            textLabel12.setFont(new Font("Monospaced", Font.BOLD, 18));
            textLabel12.setBounds(70, 610, 200, 20);

            // Añadir todos los elementos al panel
            leftPanel.add(itemLabel);
            leftPanel.add(itemLabel2);
            leftPanel.add(itemLabel3);
            leftPanel.add(itemLabel4);
            leftPanel.add(itemLabel5);
            leftPanel.add(itemLabel6);
            leftPanel.add(itemLabel7);
            leftPanel.add(itemLabel8);
            leftPanel.add(textLabel);
            leftPanel.add(textLabel2);
            leftPanel.add(textLabel3);
            leftPanel.add(textLabel4);
            leftPanel.add(textLabel5);
            leftPanel.add(textLabel6);
            leftPanel.add(textLabel7);
            leftPanel.add(textLabel8);
            leftPanel.add(textLabel9);
            leftPanel.add(textLabel10);
            leftPanel.add(textLabel11);
            leftPanel.add(textLabel12);

        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Error al cargar imágenes del panel Game Over: " + e.getMessage());
        }

        add(leftPanel); // Agregar panel al JFrame
    }
}}

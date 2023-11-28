package com.jpwmii;

import javax.swing.*;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Mario Game");
            frame.setSize(800, 600);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);

            GamePanel gamePanel = null;
            try {
                gamePanel = new GamePanel();
            } catch (IOException e) {
                e.printStackTrace();
            }
            frame.add(gamePanel);

            frame.setVisible(true);
        });
    }
}

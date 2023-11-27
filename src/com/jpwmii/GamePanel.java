package com.jpwmii;

import com.jpwmii.gameCollidableObjects.CollidableGameObject;
import com.jpwmii.gameCollidableObjects.Hole;
import com.jpwmii.gameCollidableObjects.Pipe;
import com.jpwmii.gameObjects.GameObject;
import com.jpwmii.gameObjects.Mushroom;
import com.jpwmii.interfaceObjects.Heart;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

public class GamePanel extends JComponent implements ActionListener, KeyListener {
    private final Timer timer;
    private final Image backgroundImage;
    private final Image floorTexture;
    private final Player player;
    private final ArrayList<GameObject> gameObjects = new ArrayList<>();
    private int backgroundX = 0; // X-coordinate of the background
    private int floorX = 0; // Y-coordinate of the floor
    private final ArrayList<CollidableGameObject> collidableGameObjects = new ArrayList<>();
    private final ArrayList<Heart> hearts = new ArrayList<>();

    private void resetWorld() {
        backgroundX = 0;
        floorX = 0;
        gameObjects.clear();
        collidableGameObjects.clear();

        collidableGameObjects.add(new Hole(500, 200));
        collidableGameObjects.add(new Pipe(200, 100, 100));
        hearts.add(new Heart(10, 20));
        hearts.add(new Heart(70, 20));
        hearts.add(new Heart(130, 20));
    }

    private void initializeObjects() {
        resetWorld();
    }

    public GamePanel() {
        timer = new Timer(50, this);
        timer.start();
        addKeyListener(this);
        setFocusable(true);

        backgroundImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("resources/background.jpg"))).getImage();

        floorTexture = new ImageIcon(Objects.requireNonNull(getClass().getResource("resources/floor.jpg"))).getImage();

        player = new Player();


        initializeObjects();

        try {
            player.loadMarioFrames(getClass().getResource("resources/mario.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw background
        g.drawImage(backgroundImage, -backgroundX, 0, getWidth(), getHeight() - 73, this);

        // Draw a second copy of the background for a seamless loop
        g.drawImage(backgroundImage, getWidth() - backgroundX, 0, getWidth(), getHeight() - 73, this);

        // Draw floor
        g.drawImage(floorTexture, -floorX, getHeight() - 73, getWidth(), 73, this);

        g.drawImage(floorTexture, getWidth() - floorX, getHeight() - 73, getWidth(), 73, this);

        // Draw Player

        Iterator<GameObject> iterator = gameObjects.iterator();
        while(iterator.hasNext()){
            GameObject gameObject = iterator.next();
            gameObject.draw(g);
            if(gameObject.checkIntersectWithPlayer(player)) iterator.remove();
        }
        if(collidableGameObjects.size() > 0){
            collidableGameObjects.forEach(collidableGameObject -> {
                collidableGameObject.draw(g);
                collidableGameObject.isNextToPlayerRight(player);
                collidableGameObject.isNextToPlayerLeft(player);
                collidableGameObject.isBenethPlayer(player);
                collidableGameObject.isNotBeneathPlayer(player);
            });
        }
        player.draw(g);

        if(player.getLives() > 0){
            for(int i = 0; i < player.getLives(); i++){
                hearts.get(i).draw(g);
            }
        }
        else{
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(Color.WHITE);
            g.setFont(new Font("TimesRoman", Font.PLAIN, 50));
            g.drawString("GAME OVER", getWidth() / 2 - 150, getHeight() / 2);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Update Player animation
        player.updateAnimation();

        if(player.getY() > 600){
            System.out.println(player.getY());
            player.death();
            resetWorld();
        }
        // Move background forward (right) when 'D' is pressed
        if (player.isWalkingRight()) {
            backgroundX += 7;
            floorX += 7;
            if (backgroundX >= getWidth()) {
                backgroundX = 0;
            }
            if (floorX >= getWidth()) {
                floorX = 0;
            }
            gameObjects.forEach(gameObject -> gameObject.update(gameObject.getX() - 7, gameObject.getY()));
            collidableGameObjects.forEach(collidableGameObject -> collidableGameObject.update(collidableGameObject.getX() - 7, collidableGameObject.getY()));
        }
        // Move background backward (left) when 'A' is pressed
        if (player.isWalkingLeft()) {
            backgroundX -= 7;
            floorX -= 7;
            if (backgroundX < 0) {
                backgroundX = getWidth() - 7;
            }
            if (floorX < 0) {
                floorX = getWidth() - 7;
            }
            gameObjects.forEach(gameObject -> gameObject.update(gameObject.getX() + 7, gameObject.getY()));
            collidableGameObjects.forEach(collidableGameObject -> collidableGameObject.update(collidableGameObject.getX() + 7, collidableGameObject.getY()));
        }

        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not needed in this case
    }

    @Override
    public void keyPressed(KeyEvent e) {
        player.handleKeyPress(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Handle key releases, e.g., stop character movement
        player.handleKeyRelease(e.getKeyCode());
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(800, 600); // Set the preferred size of your panel
    }
}

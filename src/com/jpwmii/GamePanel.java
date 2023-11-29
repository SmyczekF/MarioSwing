package com.jpwmii;

import com.jpwmii.gameCollidableObjects.CollidableGameObject;
import com.jpwmii.gameCollidableObjects.Hole;
import com.jpwmii.gameCollidableObjects.Pipe;
import com.jpwmii.gameObjects.*;
import com.jpwmii.interfaceObjects.Heart;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

public class GamePanel extends JComponent implements ActionListener, KeyListener {
    private final Timer timer;
    private final Timer immunityFramesTimer;
    private final Image backgroundImage;
    private final Image floorTexture;
    private final Player player;
    private final ArrayList<GameObject> gameObjects = new ArrayList<>();
    private final ArrayList<AnimatedGameObject> animatedGameObjects = new ArrayList<>();
    private final ArrayList<Enemy> enemies = new ArrayList<>();
    private int backgroundX = 0; // X-coordinate of the background
    private int floorX = 0; // Y-coordinate of the floor
    private final ArrayList<CollidableGameObject> collidableGameObjects = new ArrayList<>();
    private final ArrayList<Heart> hearts = new ArrayList<>();
    private final Image[] coinFrames = new Image[6];
    private boolean collisionRight = false;
    private boolean collisionLeft = false;
    private boolean recentlyLostLife = false;
    private boolean marioVisible = true;

    public void loadCoinFrames(java.net.URL imageUrl) throws IOException {
        BufferedImage coinImage = javax.imageio.ImageIO.read(imageUrl);
        int gapFromTheTop = 52;
        int gapBetweenFrames = 23;
        int coinWidth = 96;
        int coinHeight = 96;
        int startingGap = 5;
        for (int i = 0; i < 6; i++) {
            coinFrames[i] = coinImage.getSubimage(i * (coinWidth + gapBetweenFrames) + startingGap, gapFromTheTop, coinWidth, coinHeight);
        }
    }

    private void resetWorld() {
        backgroundX = 0;
        floorX = 0;
        gameObjects.clear();
        collidableGameObjects.clear();


        hearts.add(new Heart(10, 20));
        hearts.add(new Heart(70, 20));
        hearts.add(new Heart(130, 20));
//        animatedGameObjects.add(new Coin(300, 430, coinFrames));
//        animatedGameObjects.add(new Coin(360, 430, coinFrames));
//        animatedGameObjects.add(new Coin(420, 430, coinFrames));
//        animatedGameObjects.add(new Coin(480, 430, coinFrames));
//        collidableGameObjects.add(new Hole(550, 150));
        collidableGameObjects.add(new Pipe(500, 100, 100));
//        collidableGameObjects.add(new Pipe(1050, 100, 200));
        collidableGameObjects.add(new Pipe(900, 100, 100));
//        gameObjects.add(new Mushroom(200, 300));
        enemies.add(new Enemy(700));

    }

    private void initializeObjects() {
        resetWorld();
    }

    public GamePanel() throws IOException {
        timer = new Timer(50, this);
        immunityFramesTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                recentlyLostLife = false;
                immunityFramesTimer.stop();
                player.setY(player.getCurrentGroundLevel());
            }
        });
        timer.start();
        addKeyListener(this);
        setFocusable(true);

        backgroundImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("resources/background.jpg"))).getImage();

        floorTexture = new ImageIcon(Objects.requireNonNull(getClass().getResource("resources/floor.jpg"))).getImage();

        player = new Player();

        loadCoinFrames(getClass().getResource("resources/coin.png"));

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
        Iterator<AnimatedGameObject> animatedGameObjectIterator = animatedGameObjects.iterator();
        while(animatedGameObjectIterator.hasNext()){
            AnimatedGameObject animatedGameObject = animatedGameObjectIterator.next();
            animatedGameObject.draw(g);
            if(animatedGameObject.checkIntersectWithPlayer(player)) animatedGameObjectIterator.remove();
        }

        collisionLeft = false;
        collisionRight = false;
        collidableGameObjects.forEach(collidableGameObject -> {
            collidableGameObject.draw(g);
            if(collidableGameObject.isNextToPlayerRight(player)) collisionRight = true;
            if(collidableGameObject.isNextToPlayerLeft(player)) collisionLeft = true;
            collidableGameObject.isBenethPlayer(player);
            collidableGameObject.isNotBeneathPlayer(player);

            enemies.forEach(enemy -> {
                if(collidableGameObject.isNextToEnemyLeft(enemy)) enemy.setWalkingLeft(false);
                if(collidableGameObject.isNextToEnemyRight(enemy)) enemy.setWalkingLeft(true);
            });
        });

        enemies.forEach(enemy -> {
            enemy.draw(g);
            if(enemy.checkIntersectWithPlayer(player, recentlyLostLife)) {
                recentlyLostLife = true;
                immunityFramesTimer.start();
            }
        });

        if(recentlyLostLife){
            if(marioVisible){
                player.setY(-200);
                marioVisible = false;
            }
            else{
                player.setY(player.getCurrentGroundLevel());
                marioVisible = true;
            }
        }

        player.draw(g);

        g.drawImage(coinFrames[0], 10, 80, 50, 50, this);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 50));
        g.drawString("x " + player.getCoins(), 70, 120);

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
        if (player.isWalkingRight() && !collisionRight) {
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
            animatedGameObjects.forEach(animatedGameObject -> animatedGameObject.update(animatedGameObject.getX() - 7, animatedGameObject.getY()));
            enemies.forEach(enemy -> enemy.update(enemy.getX() - 7, enemy.getY()));
        }
        // Move background backward (left) when 'A' is pressed
        if (player.isWalkingLeft() && !collisionLeft) {
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
            animatedGameObjects.forEach(animatedGameObject -> animatedGameObject.update(animatedGameObject.getX() + 7, animatedGameObject.getY()));
            enemies.forEach(enemy -> enemy.update(enemy.getX() + 7, enemy.getY()));
        }
        animatedGameObjects.forEach(AnimatedGameObject::updateAnimation);
        enemies.forEach(Enemy::move);

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

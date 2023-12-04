package com.jpwmii;

import com.jpwmii.gameCollidableObjects.CollidableGameObject;
import com.jpwmii.gameCollidableObjects.Hole;
import com.jpwmii.gameCollidableObjects.Pipe;
import com.jpwmii.gameCollidableObjects.Platform;
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
import java.io.InputStream;
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
    private boolean marioDead = false;
    private SoundPlayer backgroundMusicPlayer;
    private boolean gameWon = false;

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
        animatedGameObjects.clear();
        enemies.clear();
        hearts.clear();

        hearts.add(new Heart(10, 20));
        hearts.add(new Heart(70, 20));
        hearts.add(new Heart(130, 20));
        animatedGameObjects.add(new Coin(250, 430, coinFrames));
        animatedGameObjects.add(new Coin(370, 430, coinFrames));
        animatedGameObjects.add(new Coin(530, 330, coinFrames));
        collidableGameObjects.add(new Platform(400, 100, 200));
        animatedGameObjects.add(new Coin(450, 40, coinFrames));
        collidableGameObjects.add(new Pipe(500, 100, 100));
        gameObjects.add(new Mushroom(500, 30));
        collidableGameObjects.add(new Platform(650, 300, 150));
        animatedGameObjects.add(new Coin(720, 430, coinFrames));
        animatedGameObjects.add(new Coin(700, 240, coinFrames));
        enemies.add(new Enemy(700));
        collidableGameObjects.add(new Pipe(900, 100, 100));
        collidableGameObjects.add(new Pipe(1050, 100, 200));
        enemies.add(new Enemy(1200));
        animatedGameObjects.add(new Coin(1200, 430, coinFrames));
        collidableGameObjects.add(new Pipe(1350, 100, 300));
        collidableGameObjects.add(new Hole(1500, 200));
        animatedGameObjects.add(new Coin(1550, 430, coinFrames));
        collidableGameObjects.add(new Platform(1600, 100, 150));
        animatedGameObjects.add(new Coin(1600, 40, coinFrames));
        animatedGameObjects.add(new Coin(1700, 40, coinFrames));
        collidableGameObjects.add(new Pipe(1800, 100, 300));
        collidableGameObjects.add(new Platform(2000, 100, 150));
        enemies.add(new Enemy(2000));
        animatedGameObjects.add(new Coin(2000, 40, coinFrames));
        animatedGameObjects.add(new Coin(2100, 40, coinFrames));
        collidableGameObjects.add(new Pipe(2200, 100, 300));
        collidableGameObjects.add(new Hole(2300, 200));
        collidableGameObjects.add(new Pipe(2500, 50, 200));
        collidableGameObjects.add(new Platform(2600, 300, 150));
        animatedGameObjects.add(new Coin(2600, 240, coinFrames));
        animatedGameObjects.add(new Coin(2700, 240, coinFrames));
        enemies.add(new Enemy(2600));
        animatedGameObjects.add(new Coin(2600, 430, coinFrames));
        animatedGameObjects.add(new Coin(2700, 430, coinFrames));
        collidableGameObjects.add(new Pipe(2800, 100, 100));
        collidableGameObjects.add(new Hole(2950, 250));
        animatedGameObjects.add(new Coin(3000, 430, coinFrames));
        collidableGameObjects.add(new Platform(3000, 100, 150));
        animatedGameObjects.add(new Coin(3000, 40, coinFrames));
        animatedGameObjects.add(new Coin(3100, 40, coinFrames));
        collidableGameObjects.add(new Pipe(3200, 100, 100));
        collidableGameObjects.add(new Pipe(3400, 200, 300));
        animatedGameObjects.add(new Coin(3400, 130, coinFrames));
        animatedGameObjects.add(new Coin(3460, 130, coinFrames));
        animatedGameObjects.add(new Coin(3520, 130, coinFrames));
        gameObjects.add(new Castle(4000));
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

        backgroundImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("resources/images/background.jpg"))).getImage();

        floorTexture = new ImageIcon(Objects.requireNonNull(getClass().getResource("resources/images/floor.jpg"))).getImage();

        player = new Player();

        loadCoinFrames(getClass().getResource("resources/images/coin.png"));

        initializeObjects();

        try {
            player.loadMarioFrames(getClass().getResource("resources/images/mario.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            InputStream inputStream = GamePanel.class.getResourceAsStream("resources/sound/soundtrack.wav");
            if (inputStream != null) {
                backgroundMusicPlayer = new SoundPlayer(inputStream);
                backgroundMusicPlayer.playLoop();
            } else {
                System.err.println("Nie udało się wczytać pliku dźwiękowego");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
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
            if(gameObject.checkIntersectWithPlayer(player)) {
                if(gameObject instanceof Castle){
                    gameWon = true;
                }
                if(gameObject.getSoundFileName() != null) playEventSound(gameObject.getSoundFileName());
                iterator.remove();
            }
        }
        Iterator<AnimatedGameObject> animatedGameObjectIterator = animatedGameObjects.iterator();
        while(animatedGameObjectIterator.hasNext()){
            AnimatedGameObject animatedGameObject = animatedGameObjectIterator.next();
            animatedGameObject.draw(g);
            if(animatedGameObject.checkIntersectWithPlayer(player)) {
                if(animatedGameObject.getSoundFileName() != null) playEventSound(animatedGameObject.getSoundFileName());
                animatedGameObjectIterator.remove();
            }
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
            if(enemy.checkIntersectWithPlayer(player, recentlyLostLife) && !marioDead) {
                playEventSound("smb_damage.wav");
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
            g.drawString("Press 'R' to restart", getWidth() / 2 - 200, getHeight() / 2 + 50);
        }

        if (gameWon) {
            backgroundMusicPlayer.stopLoop();

            Color startColor = Color.BLUE;
            Color endColor = Color.YELLOW;

            // Create a gradient paint from blue to yellow
            GradientPaint gradientPaint = new GradientPaint(0, 0, startColor, 0, getHeight(), endColor);

            // Set the paint to the graphics context
            Graphics2D g2d = (Graphics2D) g;
            g2d.setPaint(gradientPaint);

            // Fill the entire background with the gradient
            g2d.fillRect(0, 0, getWidth(), getHeight());

            // Continue with the rest of your drawing code
            g.setColor(Color.WHITE);
            g.setFont(new Font("TimesRoman", Font.PLAIN, 50));
            g.drawString("YOU WON!", getWidth() / 2 - 150, getHeight() / 2 - 100);
            g.drawString("Score: " + player.getCoins(), getWidth() / 2 - 150, getHeight() / 2 + -25);
            g.drawImage(coinFrames[0], getWidth() / 2 + 70, getHeight() / 2 - 70, 50, 50, this);
            g.drawString("Press 'R' to restart", getWidth() / 2 - 215, getHeight() / 2 + 80);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Update Player animation
        player.updateAnimation();

        if(player.getY() > 600){
            player.death();
            resetWorld();
        }
        if(player.getLives() == 0 && !marioDead){
            backgroundMusicPlayer.stopLoop();
            playEventSound("smb_mariodie.wav");
            marioDead = true;
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
        if(e.getKeyCode() == KeyEvent.VK_R){
            backgroundMusicPlayer.stopLoop();
            player.death();
            player.resetLives();
            backgroundMusicPlayer.startLoop();
            resetWorld();
            marioDead = false;
            gameWon = false;
        }
        else player.handleKeyRelease(e.getKeyCode());
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(800, 600); // Set the preferred size of your panel
    }

    private void playEventSound(String soundFileName) {
        try {
            InputStream inputStream = GamePanel.class.getResourceAsStream("resources/sound/" + soundFileName);
            if (inputStream != null) {
                new SoundPlayer(inputStream).play();
            } else {
                System.err.println("Nie udało się wczytać pliku dźwiękowego");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

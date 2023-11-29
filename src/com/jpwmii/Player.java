package com.jpwmii;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.BitSet;

public class Player {
    private int x = 50;
    private final int BASE_Y = 430;
    private int groundLevelSmall = BASE_Y;
    private int groundLevelBig = groundLevelSmall - 55;
    private int y = 430;
    private int lives = 3;
    private Image[] bigMarioWalkForwardFrames;
    private Image bigMarioStandFrame;
    private Image bigMarioJumpFrame;
    private Image[] smallMarioWalkForwardFrames;
    private Image smallMarioStandFrame;
    private Image smallMarioJumpFrame;
    private int currentFrame = 0;
    private boolean isJumping = false;
    private boolean isWalkingRight = false;
    private boolean isWalkingLeft = false;
    private boolean isSmall = true;
    private int jumpHeight = 0;
    private final int marioWidth = 57;
    private final int marioHeight = 112;
    private int coins = 0;

    Player(){}

    public void loadMarioFrames(java.net.URL imageUrl) throws IOException {
        BufferedImage marioImage = javax.imageio.ImageIO.read(imageUrl);
        bigMarioWalkForwardFrames = new Image[3];
        smallMarioWalkForwardFrames = new Image[3];
        int gapFromTheTop = 26;
        for (int i = 0; i < 3; i++) {
            int gapBetweenFrames = 27;
            bigMarioWalkForwardFrames[i] = marioImage.getSubimage(i * (marioWidth + gapBetweenFrames) + gapBetweenFrames, gapFromTheTop, marioWidth, marioHeight);
            smallMarioWalkForwardFrames[i] = marioImage.getSubimage(i * (marioWidth + gapBetweenFrames) + gapBetweenFrames, gapFromTheTop + 138, marioWidth, marioWidth);
        }
        bigMarioStandFrame = marioImage.getSubimage(445, gapFromTheTop, marioWidth, marioHeight);
        smallMarioStandFrame = marioImage.getSubimage(445, gapFromTheTop + 138, marioWidth, marioWidth);
        bigMarioJumpFrame = marioImage.getSubimage(362, gapFromTheTop, marioWidth, marioHeight);
        smallMarioJumpFrame = marioImage.getSubimage(362, gapFromTheTop + 138, marioWidth, marioWidth);
    }

    private void drawNextFrame(Graphics g, Image jumpFrame, Image[] walkFrames, Image standFrame, int height) {
        if (isJumping && isWalkingRight) {
            g.drawImage(jumpFrame, x, y, null);
        }
        else if (isJumping && isWalkingLeft) {
            g.drawImage(jumpFrame, x + marioWidth, y, -marioWidth, height, null);
        }
        else if (isWalkingRight) {
            g.drawImage(walkFrames[currentFrame], x, y, null);
        }
        else if (isWalkingLeft) {
            g.drawImage(walkFrames[currentFrame], x + marioWidth, y, -marioWidth, height, null);
        }
        else {
            g.drawImage(standFrame, x, y, null);
        }
    }

    public void draw(Graphics g) {
        if (isJumping) {
            y -= jumpHeight;
            jumpHeight--;
            if(isSmall) {
                if (y >= groundLevelSmall) {
                    isJumping = false;
                    y = groundLevelSmall;
                }
            }
            else {
                System.out.println(y + " " + groundLevelBig);
                if (y >= groundLevelBig) {
                    isJumping = false;
                    y = groundLevelBig;
                }
            }
        }
        if(isSmall) drawNextFrame(g, smallMarioJumpFrame, smallMarioWalkForwardFrames, smallMarioStandFrame, marioWidth);
        else drawNextFrame(g, bigMarioJumpFrame, bigMarioWalkForwardFrames, bigMarioStandFrame, marioHeight);

    }

    public void updateAnimation() {
        currentFrame = (currentFrame + 1) % 3;
    }

    public void moveForward() {
        isWalkingRight = true;
    }

    public void moveBackward() {
        isWalkingLeft = true;
    }

    public void jump() {
        if (!isJumping) {
            isJumping = true;
            jumpHeight = 20;
        }
    }

    public void handleKeyPress(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_W -> jump();
            case KeyEvent.VK_A -> moveBackward();
            case KeyEvent.VK_D -> moveForward();
        }
    }

    public void handleKeyRelease(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_A -> isWalkingLeft = false;
            case KeyEvent.VK_D -> isWalkingRight = false;
        }
    }

    public boolean isWalkingRight() {
        return isWalkingRight;
    }

    public boolean isWalkingLeft() {
        return isWalkingLeft;
    }

    public void setBig() {
        isSmall = false;
        y = groundLevelBig;
    }

    public void setSmall() {
        isSmall = true;
        y = groundLevelSmall;
    }

    public void setGroundLevel(int x) {
        System.out.println(x);
        groundLevelSmall = x;
        groundLevelBig = x - 55;
    }

    public void resetGroundLevel() {
        groundLevelSmall = BASE_Y;
        groundLevelBig = BASE_Y - 55;
    }

    public int getHeight() {
        System.out.println(isSmall);
        if(isSmall)
            return marioWidth;
        else
            return marioHeight + 26;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public int getLives() {
        return lives;
    }

    public void addCoin() {
        coins++;
    }

    public int getCoins() {
        return coins;
    }

    public void setIsJumping(boolean isJumping) {
        this.isJumping = isJumping;
    }

    public void death() {
        lives = 0;
        x = 50;
        y = 430;
        isJumping = false;
        isWalkingRight = false;
        isWalkingLeft = false;
        isSmall = true;
        jumpHeight = 0;
        currentFrame = 0;
        coins = 0;
        resetGroundLevel();
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getCurrentGroundLevel(){
        if(isSmall)
            return groundLevelSmall;
        else
            return groundLevelBig;
    }

    public void looseLife() {
        lives--;
    }

    public void printPlayerInfo() {
        System.out.println("Lives: " + lives);
        System.out.println("X: " + x);
        System.out.println("Y: " + y);
        System.out.println("isJumping: " + isJumping);
        System.out.println("isWalkingRight: " + isWalkingRight);
        System.out.println("isWalkingLeft: " + isWalkingLeft);
        System.out.println("isSmall: " + isSmall);
        System.out.println("jumpHeight: " + jumpHeight);
        System.out.println("currentFrame: " + currentFrame);
        System.out.println("groundLevelSmall: " + groundLevelSmall);
        System.out.println("groundLevelBig: " + groundLevelBig);
    }

    public Rectangle getBounds() {
        if(isSmall)
            return new Rectangle(x, y, marioWidth, marioWidth);
        else
            return new Rectangle(x, y, marioWidth, marioHeight);
    }
}

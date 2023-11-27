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
    private boolean canMoveRight = true;
    private boolean canMoveLeft = true;

    Player(){}
    Player(int lives) {
        this.lives = lives;
    }

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
        if(canMoveRight) isWalkingRight = true;
        else isWalkingRight = false;
    }

    public void moveBackward() {
        if(canMoveLeft) isWalkingLeft = true;
        else isWalkingLeft = false;
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
        groundLevelSmall = x;
        groundLevelBig = x - 55;
    }

    public void resetGroundLevel() {
        groundLevelSmall = BASE_Y;
        groundLevelBig = BASE_Y - 55;
    }

    public void setCanMoveRight(boolean canMoveRight) {
        this.canMoveRight = canMoveRight;
        if(!canMoveRight) isWalkingRight = false;
    }

    public int getHeight() {
        if(isSmall)
            return marioWidth;
        else
            return marioHeight;
    }

    public void setCanMoveLeft(boolean canMoveLeft) {
        this.canMoveLeft = canMoveLeft;
        if(!canMoveLeft) isWalkingLeft = false;
    }

    public int getY() {
        return y;
    }

    public int getLives() {
        return lives;
    }

    public void setIsJumping(boolean isJumping) {
        this.isJumping = isJumping;
    }

    public void death() {
        lives--;
        x = 50;
        y = 430;
        isJumping = false;
        isWalkingRight = false;
        isWalkingLeft = false;
        isSmall = true;
        jumpHeight = 0;
        currentFrame = 0;
        canMoveRight = true;
        canMoveLeft = true;
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
        System.out.println("canMoveRight: " + canMoveRight);
        System.out.println("canMoveLeft: " + canMoveLeft);
        System.out.println("groundLevelSmall: " + groundLevelSmall);
        System.out.println("groundLevelBig: " + groundLevelBig);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, marioWidth, marioHeight);
    }
}

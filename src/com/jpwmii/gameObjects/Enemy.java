package com.jpwmii.gameObjects;

import com.jpwmii.Player;

import javax.swing.*;
import java.util.Objects;

public class Enemy extends GameObject{

    private boolean isWalkingLeft = true;

    public Enemy(int x) {
        super(x, 440, 50, 50, new ImageIcon(Objects.requireNonNull(Enemy.class.getResource("../resources/images/goomba.gif"))).getImage());
    }

    public boolean checkIntersectWithPlayer(Player player, boolean RecentlyLostLife){
        if (getBounds().intersects(player.getBounds())) {
            return intersectsWithPlayer(player, RecentlyLostLife);
        }
        return false;
    }

    public boolean intersectsWithPlayer(Player player, boolean RecentlyLostLife) {
        if (RecentlyLostLife) {
            return false;
        }
        player.looseLife();
        return true;
    }

    public void moveLeft() {
        x -= 3;
    }

    public void moveRight() {
        x += 3;
    }

    public void move() {
        if(isWalkingLeft) {
            moveLeft();
        }
        else {
            moveRight();
        }
    }

    public void setWalkingLeft(boolean isWalkingLeft) {
        this.isWalkingLeft = isWalkingLeft;
    }
}

package com.jpwmii.gameCollidableObjects;

import com.jpwmii.Player;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class Hole extends CollidableGameObject{
    public Hole(int x, int width) {
        super(x, 488, width, 200, new ImageIcon(Objects.requireNonNull(Hole.class.getResource("../resources/images/hole.jpg"))).getImage());
    }

    @Override
    public boolean isNextToPlayerRight(Player player) {
        return false;
    }

    @Override
    public boolean isNextToPlayerLeft(Player player) {
        return false;
    }

    @Override
    public void isNotBeneathPlayer(Player player) {
    }

    @Override
    public void isBenethPlayer(Player player) {
        if(player.getBounds() != null && player.getBounds().intersects(new Rectangle(x + 40, y - 7, width - 80, height))) {
            player.setIsJumping(true);
            player.setGroundLevel(800);
            isBenethPlayer = true;
        }
        if(isBenethPlayer){
            if(player.getBounds() != null && !player.getBounds().intersects(new Rectangle(x + 40, y - 7, width - 80, height))) {
                player.resetGroundLevel();
                isBenethPlayer = false;
            }
        }
    }
}

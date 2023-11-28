package com.jpwmii.gameCollidableObjects;

import com.jpwmii.gameObjects.GameObject;
import com.jpwmii.Player;

import java.awt.*;

public abstract class CollidableGameObject extends GameObject {

    private boolean isBenethPlayer = false;

    public CollidableGameObject(int x, int y, int width, int height, Image image) {
        super(x, y, width, height, image);
    }

    public void isNextToPlayerRight(Player player) {
        if(!isBenethPlayer)
            player.setCanMoveRight(player.getBounds() == null || !player.getBounds().intersects(new Rectangle(x - 7, y, width, height - 10)));
    }

    public void isNextToPlayerLeft(Player player) {
        if(!isBenethPlayer)
            player.setCanMoveLeft(player.getBounds() == null || !player.getBounds().intersects(new Rectangle(x + 7, y, width, height - 10)));
    }
    public void isBenethPlayer(Player player) {
        if(player.getBounds() != null && player.getBounds().intersects(new Rectangle(x , y- 50 , width, 50))) {
            player.setGroundLevel(y - 57);
            isBenethPlayer = true;

        }
    }

    public void isNotBeneathPlayer(Player player) {
        if(isBenethPlayer){
            if(player.getBounds() != null && !player.getBounds().intersects(new Rectangle(x, y - 50, width, 50))) {
                System.out.println("not beneath");
                player.setIsJumping(true);
                player.resetGroundLevel();
                isBenethPlayer = false;
            }
        }
    }
}

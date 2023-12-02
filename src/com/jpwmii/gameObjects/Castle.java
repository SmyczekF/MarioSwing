package com.jpwmii.gameObjects;

import com.jpwmii.Player;

import javax.swing.*;
import java.util.Objects;

public class Castle extends GameObject{
    public Castle(int x) {
        super(x, 90, 400, 400, new ImageIcon(Objects.requireNonNull(Castle.class.getResource("../resources/images/castle.png"))).getImage(), "smb_world_clear.wav");
    }

    @Override
    public void intersectsWithPlayer(Player player) {
        System.out.println("You won!");
    }
}

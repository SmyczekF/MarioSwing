package com.jpwmii.gameObjects;

import com.jpwmii.Player;

import javax.swing.*;
import java.util.Objects;

public class Mushroom extends GameObject {
    public Mushroom(int x, int y) {
        super(x, y, 57, 57, new ImageIcon(Objects.requireNonNull(Mushroom.class.getResource("../resources/images/mushroom.png"))).getImage(), "mushroom.wav");
    }

    @Override
    public void intersectsWithPlayer(Player player) {
        player.setBig();
    }
}

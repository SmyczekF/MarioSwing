package com.jpwmii.gameCollidableObjects;

import com.jpwmii.Player;

import javax.swing.*;
import java.util.Objects;

public class Platform extends CollidableGameObject {

    public Platform(int x, int y, int width) {
        super(x, y, width, 35, new ImageIcon(Objects.requireNonNull(Platform.class.getResource("../resources/images/platform.png"))).getImage());
    }

    @Override
    public boolean isNextToPlayerRight(Player player) {
        return false;
    }

    @Override
    public boolean isNextToPlayerLeft(Player player) {
        return false;
    }
}

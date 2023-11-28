package com.jpwmii.gameObjects;

import com.jpwmii.Player;

import java.awt.*;

public class Coin extends AnimatedGameObject {

    public Coin(int x, int y, Image[] frames) {
        super(x, y, 40, 40, frames);
    }

    @Override
    public void intersectsWithPlayer(Player player) {
        player.addCoin();
    }
}

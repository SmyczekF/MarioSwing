package com.jpwmii.gameObjects;

import com.jpwmii.Player;

import javax.swing.*;
import java.util.Objects;

public class Coin extends GameObject {
    public Coin(int x, int y) {
        super(x, y, 32, 32, new ImageIcon(Objects.requireNonNull(Coin.class.getResource("../resources/coin.png"))).getImage());
    }

    public void intersectsWithPlayer(Player player) {
//        player.addCoin();
    }
}

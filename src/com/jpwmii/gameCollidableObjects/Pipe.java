package com.jpwmii.gameCollidableObjects;

import javax.swing.*;
import java.util.Objects;

public class Pipe extends CollidableGameObject{
    public Pipe(int x, int width, int height) {
        super(x, 488 - height, width, height, new ImageIcon(Objects.requireNonNull(Pipe.class.getResource("../resources/pipe.png"))).getImage());
    }
}

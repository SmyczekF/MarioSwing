package com.jpwmii.gameObjects;

import java.awt.*;
import java.io.IOException;

public abstract class AnimatedGameObject extends GameObject{

    protected Image[] frames;
    private int currentFrame = 0;

    public AnimatedGameObject(int x, int y, int width, int height, Image[] frames) {
        super(x, y, width, height, (Image) null);
        this.frames = frames;
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(frames[currentFrame], x, y, width, height, null);
    }

    public void updateAnimation() {
        currentFrame = (currentFrame + 1) % 6;
    }
}

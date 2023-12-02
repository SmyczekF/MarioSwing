package com.jpwmii.gameObjects;

import java.awt.*;

public abstract class AnimatedGameObject extends GameObject{

    protected Image[] frames;
    protected int currentFrame = 0;

    public AnimatedGameObject(int x, int y, int width, int height, Image[] frames) {
        super(x, y, width, height, null);
        this.frames = frames;
    }

    public AnimatedGameObject(int x, int y, int width, int height, Image[] frames, String soundFileName) {
        super(x, y, width, height, null, soundFileName);
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

package com.jpwmii.interfaceObjects;

import java.awt.*;

public abstract class InterfaceObject {

    protected int x;
    protected int y;
    protected int width;
    protected int height;

    public InterfaceObject(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public abstract void draw(Graphics g);
}

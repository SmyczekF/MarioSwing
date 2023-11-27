package com.jpwmii.interfaceObjects;

import java.awt.*;
import java.awt.geom.GeneralPath;

public class Heart extends InterfaceObject {

    public Heart(int x, int y) {
        super(x, y, 50, 50);
    }

    public void draw(Graphics g) {
        g.setColor(Color.RED);
        int[] triangleX = {
                x - 2*width/18,
                x + width + 2*width/18,
                (x - 2*width/18 + x + width + 2*width/18)/2};
        int[] triangleY = {
                y + height - 2*height/3,
                y + height - 2*height/3,
                y + height };
        g.fillOval(
                x - width/12,
                y,
                width/2 + width/6,
                height/2);
        g.fillOval(
                x + width/2 - width/12,
                y,
                width/2 + width/6,
                height/2);
        g.fillPolygon(triangleX, triangleY, triangleX.length);

    }
}

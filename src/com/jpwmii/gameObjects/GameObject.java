package com.jpwmii.gameObjects;

import com.jpwmii.Player;

import java.awt.*;

public abstract class GameObject {
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected Image image;
    public GameObject(int x, int y, int width, int height, Image image) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.image = image;
    }

    public void draw(Graphics g) {

        g.drawImage(image, x, y, width, height, null);
    }

    public void update(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean checkIntersectWithPlayer(Player player){
        if (getBounds().intersects(player.getBounds())) {
            intersectsWithPlayer(player);
            return true;
        }
        return false;
    };


    public void intersectsWithPlayer(Player player){
        //empty body in case of colloidal objects, which do not intent to intersect with player
    };

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    // Other methods, getters, and setters as needed
}

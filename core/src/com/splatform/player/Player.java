package com.splatform.player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.splatform.gravity.JumpingObject;

public class Player extends JumpingObject {
    private int width;
    private int height;
    private SpriteBatch body;
    private Texture img;
    private int speed;

    public Player() {}

    public Player(int x, int y) {
        this(x, y, 6, new Vector2(0, 20));
    }

    public Player(int x, int y, int speed, Vector2 jumpVelocity) {
        super(x, y, jumpVelocity);
        this.speed = speed;
        body = new SpriteBatch();
        img = new Texture("badlogic.jpg");
        width = img.getWidth();
        height = img.getHeight();
    }

    public int getSpeed() {
        return this.speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public SpriteBatch getBody() {
        return body;
    }
    
    public void setBody(SpriteBatch body) {
        this.body = body;
    }

    public Texture getImg() {
        return img;
    }

    public void setImg(Texture img) {
        this.img = img;
    }

    public void render() {
        body.begin();
        body.draw(img, x, y);
        body.end();
    }

    public void debug() {
        System.out.println("Character: " + this);
        System.out.println("X: " + x);
        System.out.println("Y: " + y);
        System.out.println("Acceleration: " + acceleration);
        System.out.println("Velocity: " + velocity);
        System.out.println("Jump Velocity: " + jumpVelocity);
    }
}

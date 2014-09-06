package com.splatform.player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.splatform.level.LevelObject;
import com.splatform.physics.IFaller;
import com.splatform.physics.IJumper;

public class Player extends LevelObject implements IFaller, IJumper {
    private int width;
    private int height;
    private SpriteBatch body;
    private Texture img;
    private Vector2 runVelocity;
    private Vector2 jumpVelocity;
    private boolean isJumping;
    private boolean isFalling;
    private float fallTime;

    public Player() {}

    public Player(int xPosition, int yPosition) {
        this(xPosition, yPosition, new Vector2(6, 0), new Vector2(0, 20));
    }

    public Player(int xPosition, int yPosition, Vector2 runVelocity, Vector2 jumpVelocity) {
        super(xPosition, yPosition);
        this.jumpVelocity = jumpVelocity;
        this.runVelocity = runVelocity;
        body = new SpriteBatch();
        img = new Texture("Dale(no_background).png");
        width = img.getWidth();
        height = img.getHeight();
    }

    public Vector2 getRunVelocity() {
        return this.runVelocity;
    }

    public void setRunVelocity(Vector2 runVelocity) {
        this.runVelocity = runVelocity;
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
        body.draw(img, xPosition, yPosition);
        body.end();
    }

    public void debug() {
        System.out.println("Character: " + this);
        System.out.println("X: " + xPosition);
        System.out.println("Y: " + yPosition);
        System.out.println("Acceleration: " + acceleration);
        System.out.println("Velocity: " + velocity);
        System.out.println("Jump Velocity: " + jumpVelocity);
    }

    @Override
    public boolean isFalling() {
        return isFalling;
    }

    @Override
    public void setFalling(boolean isFalling) {
        this.isFalling = isFalling;
        if (!isFalling) {
            velocity.y = 0;
            resetFallTime();
        }
    }

    @Override
    public float getFallTime() {
        return fallTime;
    }

    @Override
    public void setFallTime(float fallTime) {
        this.fallTime = fallTime;
    }

    @Override
    public void resetFallTime() {
        fallTime = 0;
    }

    @Override
    public void fall(float delta) {
        this.fallTime += delta;
        velocity.add(0, GRAVITY * fallTime);
    }

    @Override
    public Vector2 getJumpVelocity() {
        return jumpVelocity;
    }

    @Override
    public void setJumpVelocity(Vector2 jumpVelocity) {
        this.jumpVelocity = jumpVelocity;
    }

    @Override
    public boolean isJumping() {
        return isJumping;
    }

    @Override
    public void setJumping(boolean isJumping) {
        this.isJumping = isJumping;
    }

    @Override
    public void jump() {
        if (!isJumping) {
            isFalling = true;
            isJumping = true;
            velocity.add(jumpVelocity);
        }
    }
}

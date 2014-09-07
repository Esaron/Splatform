package com.splatform.player;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.splatform.level.LevelObject;
import com.splatform.physics.IFaller;
import com.splatform.physics.IFlyer;
import com.splatform.physics.IJumper;
import com.splatform.rendering.WorldRenderer;

public class Player extends LevelObject implements IFaller, IJumper, IFlyer {
    private int width;
    private int height;
    private SpriteBatch body;
    private Sprite img;
    private Vector2 runVelocity;
    private Vector2 jumpVelocity;
    private Vector2 flyVelocity = new Vector2(0, 20);
    private boolean isJumping;
    private boolean isFalling;
    private boolean isFlying;
    private float fallTime;
    private float flyTime;
    private float maxFlyTime = 3;

    public Player() {}

    public Player(int xPosition, int yPosition) {
        this(xPosition, yPosition, new Vector2(6, 0), new Vector2(0, 20));
    }

    public Player(int xPosition, int yPosition, Vector2 runVelocity, Vector2 jumpVelocity) {
        super(xPosition, yPosition);
        this.jumpVelocity = jumpVelocity;
        this.runVelocity = runVelocity;
        body = new SpriteBatch();
        img = WorldRenderer.TEXTURES.createSprite("Dale");
        width = (int) Math.ceil(img.getWidth());
        height = (int) Math.ceil(img.getHeight());
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

    public Sprite getImg() {
        return img;
    }

    public void setImg(Sprite img) {
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
        setFalling(true);
        setJumping(true);
        velocity.add(jumpVelocity);
    }
    
    
    public void jumpOrFly() {
        if (!isJumping && !isFalling) {
            jump();
        }
        else {
            setFlying(true);
        }
    }

    @Override
    public boolean isFlying() {
        return isFlying;
    }

    @Override
    public void setFlying(boolean isFlying) {
        this.isFlying = isFlying;
        setFalling(!isFlying);
        if(!isFlying){
            resetFlyTime();
        }
        else {
            setJumping(false);
        }
    }

    @Override
    public float getFlyTime() {
        return flyTime;
    }

    @Override
    public void setFlyTime(float flyTime) {
        this.flyTime = flyTime;
    }
    
    @Override
    public float getMaxFlyTime() {
        return maxFlyTime;
    }
    
    @Override
    public void setMaxFlyTime(float maxFlyTime) {
        this.maxFlyTime = maxFlyTime;
    }

    @Override
    public void resetFlyTime() {
        flyTime = 0;
    }

    @Override
    public void fly(float delta) {
        if(!isFlying){
            setFlying(true);
        }
        move(0, (int) (flyVelocity.y * Math.ceil(delta)));
        flyTime += delta;
        System.out.println(flyTime);
    }
}

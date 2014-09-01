package com.splatform.level;

import com.badlogic.gdx.math.Vector2;

public abstract class LevelObject {

    protected int x;
    protected int y;
    protected Vector2 acceleration = new Vector2();
    protected Vector2 velocity = new Vector2();
    protected boolean isMoving;
    protected float accelerationTimeX;
    protected float accelerationTimeY;

    public LevelObject() {}

    public LevelObject(int x, int y) {
        this(x, y, new Vector2(), new Vector2());
    }

    public LevelObject(Vector2 acceleration, Vector2 velocity) {
        this(0, 0, acceleration, velocity);
    }

    public LevelObject(int x, int y, Vector2 acceleration, Vector2 velocity) {
        this.x = x;
        this.y = y;
        this.acceleration = acceleration;
        this.velocity = velocity;
    }

    public Vector2 getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(Vector2 acceleration) {
        this.acceleration = acceleration;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    public int getX() {
        return x;
    }
    
    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }
    
    public void setY(int y) {
        this.y = y;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void move(int x, int y) {
        this.x += x;
        this.y += y;
    }

    public boolean isMoving() {
        return isMoving;
    }

    public void setMoving(boolean isMoving) {
        this.isMoving = isMoving;
    }

    public float getAccelerationTimeX() {
        return accelerationTimeX;
    }

    public float getAccelerationTimeY() {
        return accelerationTimeY;
    }

    public void setAccelerationTime(float x, float y) {
        accelerationTimeX = x;
        accelerationTimeY = y;
    }

    public void resetAccelerationTime() {
        setAccelerationTime(0, 0);
    }

    public void resetAccelerationTimeX() {
        accelerationTimeX = 0;
    }

    public void resetAccelerationTimeY() {
        accelerationTimeY = 0;
    }

    public void accelerate(float time) {
        accelerationTimeX += time;
        accelerationTimeY += time;
        velocity.add(acceleration.x * accelerationTimeX,
                acceleration.y * accelerationTimeY);
        if (acceleration.x == 0) {
            resetAccelerationTimeX();
        }
        if (acceleration.y == 0) {
            resetAccelerationTimeY();
        }
        if (Math.abs(velocity.x) + Math.abs(velocity.y) > 0) {
            isMoving = true;
        }
        else {
            isMoving = false;
        }
    }
}

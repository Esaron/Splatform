package com.splatform.level;

import com.badlogic.gdx.math.Vector2;

public abstract class LevelObject {

    protected int xPosition;
    protected int yPosition;
    protected Vector2 acceleration = new Vector2();
    protected Vector2 velocity = new Vector2();
    protected boolean isMoving;
    protected float accelerationTimeX;
    protected float accelerationTimeY;

    public LevelObject() {}

    public LevelObject(int xPosition, int yPosition) {
        this(xPosition, yPosition, new Vector2(), new Vector2());
    }

    public LevelObject(int xPosition, int yPosition, Vector2 acceleration, Vector2 velocity) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
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

    public float getX() {
        return xPosition;
    }
    
    public void setX(int x) {
        xPosition = x;
    }

    public float getY() {
        return yPosition;
    }
    
    public void setY(int y) {
        yPosition = y;
    }

    public void setPosition(int x, int y) {
        xPosition = x;
        yPosition = y;
    }

    public void move(int x, int y) {
        xPosition += x;
        yPosition += y;
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

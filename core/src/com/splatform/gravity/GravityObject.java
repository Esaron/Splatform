package com.splatform.gravity;

import com.badlogic.gdx.math.Vector2;
import com.splatform.levelObject.LevelObject;

public abstract class GravityObject extends LevelObject {
    // Gravity on earth = 9.8 m/pow(s, 2)
    public static float GRAVITY = -9.81F;

    protected boolean isFalling;
    protected float fallAccelerationTime;

    public GravityObject() {}

    public GravityObject(int x, int y) {
        this(x, y, new Vector2(), new Vector2());
    }

    public GravityObject(Vector2 acceleration, Vector2 velocity) {
        this(0, 0, acceleration, velocity);
    }

    public GravityObject(int x, int y, Vector2 acceleration, Vector2 velocity) {
        super(x, y, acceleration, velocity);
    }

    public boolean isFalling() {
        return isFalling;
    }

    public void setFalling(boolean isFalling) {
        this.isFalling = isFalling;
        if (!isFalling) {
            velocity.y = 0;
            fallAccelerationTime = 0;
        }
    }

    public float getFallAccelerationTime() {
        return fallAccelerationTime;
    }

    public void setFallAccelerationTime(float fallAccelerationTime) {
        this.fallAccelerationTime = fallAccelerationTime;
    }

    public void resetFallAccelerationTime() {
        this.fallAccelerationTime = 0;
    }

    public void fall(float delta) {
        this.fallAccelerationTime += delta;
        velocity.add(0, GRAVITY * fallAccelerationTime);
    }
}
package com.splatform.physics;

public interface IFaller {
    // Gravity on earth = 9.8 m/pow(s, 2)
    public static float GRAVITY = -9.81F;

    public boolean isFalling();

    public void setFalling(boolean isFalling);

    public float getFallTime();

    public void setFallTime(float fallTime);

    public void resetFallTime();

    public void fall(float delta);
}
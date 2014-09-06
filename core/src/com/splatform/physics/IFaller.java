package com.splatform.physics;

import com.badlogic.gdx.math.Vector2;
import com.splatform.level.LevelObject;

public interface IFaller {
    // Gravity on earth = 9.8 m/pow(s, 2)
    public static float GRAVITY = -9.81F;

    public boolean isFalling();

    public void setFalling(boolean isFalling);

    public float getFallTime();

    public void setFallTime(float fallAccelerationTime);

    public void resetFallTime();

    public void fall(float delta);
}
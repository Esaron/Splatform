package com.splatform.physics;

import com.badlogic.gdx.math.Vector2;

public interface IJumper {

    public Vector2 getJumpVelocity();

    public void setJumpVelocity(Vector2 jumpVelocity);

    public boolean isJumping();

    public void setJumping(boolean isJumping);

    public void jump();
}

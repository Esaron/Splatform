package com.splatform.gravity;

import com.badlogic.gdx.math.Vector2;

public abstract class JumpingObject extends GravityObject {
    protected Vector2 jumpVelocity = new Vector2(0F, 10F);

    protected JumpingObject() {}

    protected JumpingObject(int x, int y) {
        this(x, y, new Vector2(), new Vector2(), new Vector2(0F, 10F));
    }

    protected JumpingObject(Vector2 jumpVelocity) {
        this(0, 0, new Vector2(), new Vector2(), jumpVelocity);
    }

    protected JumpingObject(int x, int y, Vector2 jumpVelocity) {
        this(x, y, new Vector2(), new Vector2(), jumpVelocity);
    }

    protected JumpingObject(Vector2 acceleration, Vector2 velocity, Vector2 jumpVelocity) {
        this(0, 0, acceleration, velocity, jumpVelocity);
    }

    public JumpingObject(int x, int y, Vector2 acceleration, Vector2 velocity, Vector2 jumpVelocity) {
        super(x, y, acceleration, velocity);
        this.jumpVelocity = jumpVelocity;
    }

    public Vector2 getJumpVelocity() {
        return jumpVelocity;
    }

    public void setJumpVelocity(Vector2 jumpVelocity) {
        this.jumpVelocity = jumpVelocity;
    }

    public void jump() {
        if (!isFalling) {
            isFalling = true;
            velocity.add(jumpVelocity);
        }
    }
}

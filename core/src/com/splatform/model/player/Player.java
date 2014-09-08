package com.splatform.model.player;

import com.badlogic.gdx.math.Vector2;
import com.splatform.model.world.VisibleObject;
import com.splatform.view.WorldRenderer;

public class Player extends VisibleObject {
    
    private static final String newline = System.getProperty("line.separator");

    /**
     * This enum is used to track the state of the player
     * The player will only ever be in one state at a time
     */
    public enum State {
        STANDING,
        RUNNING,
        JUMPING,
        FLYING
    }
    
    // The current state that the player is in
    private State state = State.STANDING;
    
    // The amount of time the player has been in its current state
    private float stateTime;
    
    // The speeds at which the player runs, jumps, and flies
    private float runVelocity = 5f;
    private float jumpVelocity = 8f;
    private float flyVelocity = 8f;
    
    // The maximum amount of time the player can fly for before having to land
    private float maxFlyTime = 3000f;
    
    // The maximum amount of time the player can jump for before having to land/fly
    private float maxJumpTime = 250f;

    /**
     * Creates a new player at the provided position with the provided width and height
     * 
     * @param position The initial (x, y) position of the player
     * @param width The width of the player's bounding box
     * @param height The height of the player's bounding box
     */
    public Player(Vector2 position, float width, float height) {
        super(position, width, height);
        img = WorldRenderer.TEXTURES.createSprite("Dale");
        img.setSize(width, height);
    }
    
    public void setSize(float width, float height) {
        bounds.width = width;
        bounds.height = height;
        img.setSize(bounds.width, bounds.height);
    }
    
    public State getState() {
        return state;
    }
    
    public void setState(State state) {
        this.state = state;
        stateTime = 0f;
    }
    
    public float getStateTime() {
        return stateTime;
    }
    
    public void setStateTime(float stateTime) {
        this.stateTime = stateTime;
    }

    public float getRunVelocity() {
        return this.runVelocity;
    }

    public void setRunVelocity(float runVelocity) {
        this.runVelocity = runVelocity;
    }

    public float getJumpVelocity() {
        return this.jumpVelocity;
    }

    public void setJumpVelocity(float jumpVelocity) {
        this.jumpVelocity = jumpVelocity;
    }

    public float getFlyVelocity() {
        return this.flyVelocity;
    }

    public void setFlyVelocity(float flyVelocity) {
        this.flyVelocity = flyVelocity;
    }

    public float getMaxFlyTime() {
        return maxFlyTime;
    }
    
    public void setMaxFlyTime(float maxFlyTime) {
        this.maxFlyTime = maxFlyTime;
    }

    public float getMaxJumpTime() {
        return maxJumpTime;
    }
    
    public void setMaxJumpTime(float maxJumpTime) {
        this.maxJumpTime = maxJumpTime;
    }

    public String toString() {
        return "State: " + state + newline +
               "Position: " + position + newline +
               "Velocity: " + velocity + newline +
               "Acceleration: " + acceleration + newline;
    }
}
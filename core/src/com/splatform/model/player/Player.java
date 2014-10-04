package com.splatform.model.player;

import java.util.ArrayList;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.splatform.model.world.VisibleObject;
import com.splatform.view.WorldRenderer;

public class Player extends VisibleObject {
    
    private static final String newline = System.getProperty("line.separator");

    private static final float FPS_SCL = 1/3f;

    /**
     * This enum is used to track the state of the player
     * The player will only ever be in one state at a time
     */
    public enum State {
        STANDING,
        RUNNING_LEFT,
        RUNNING_RIGHT,
        JUMPING,
        FLYING
    }
    
    // The current state that the player is in
    private State state = State.STANDING;
    
    // The amount of time the player has been in its current state
    private float stateTime;
    
    // The speeds at which the player runs, jumps, and flies
    private float runVelocity = 400f;
    private float jumpVelocity = 550f;
    private float flyVelocity = 550f;
    
    // The maximum amount of time the player can fly for before having to land
    private float maxFlyTime = 3000f;
    
    // The maximum amount of time the player can jump for before having to land/fly
    private float maxJumpTime = 250f;

    private Animation leftFrames;
    private Animation rightFrames;
    private Animation jumpFrames;
    private Animation flyFrames;

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
        leftFrames = new Animation(FPS_SCL, WorldRenderer.TEXTURES.createSprite("badlogic"),
                WorldRenderer.TEXTURES.createSprite("metal_plate"));
        leftFrames.setPlayMode(PlayMode.LOOP);
        rightFrames = new Animation(FPS_SCL, WorldRenderer.TEXTURES.createSprite("badlogic"),
                WorldRenderer.TEXTURES.createSprite("metal_plate"));
        rightFrames.setPlayMode(PlayMode.LOOP);
        jumpFrames = new Animation(FPS_SCL, WorldRenderer.TEXTURES.createSprite("badlogic"),
                WorldRenderer.TEXTURES.createSprite("metal_plate"));
        jumpFrames.setPlayMode(PlayMode.LOOP);
        flyFrames = new Animation(FPS_SCL, WorldRenderer.TEXTURES.createSprite("badlogic"),
                WorldRenderer.TEXTURES.createSprite("metal_plate"));
        flyFrames.setPlayMode(PlayMode.LOOP);
    }
    
    public void setSize(float width, float height) {
        bounds.width = width;
        bounds.height = height;
        img.setSize(bounds.width, bounds.height);
        for (TextureRegion frame : leftFrames.getKeyFrames()) {
            frame.setRegionWidth((int) bounds.width);
            frame.setRegionHeight((int)bounds.height);
        }
        for (TextureRegion frame : rightFrames.getKeyFrames()) {
            frame.setRegionWidth((int) bounds.width);
            frame.setRegionHeight((int)bounds.height);
        }
        for (TextureRegion frame : jumpFrames.getKeyFrames()) {
            frame.setRegionWidth((int) bounds.width);
            frame.setRegionHeight((int)bounds.height);
        }
        for (TextureRegion frame : flyFrames.getKeyFrames()) {
            frame.setRegionWidth((int) bounds.width);
            frame.setRegionHeight((int)bounds.height);
        }
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
    
    public TextureRegion getCurrentFrame() {
        switch(state) {
            case RUNNING_LEFT:
                return leftFrames.getKeyFrame(stateTime);
            case RUNNING_RIGHT:
                return rightFrames.getKeyFrame(stateTime);
            case JUMPING:
                return jumpFrames.getKeyFrame(stateTime);
            case FLYING:
                return flyFrames.getKeyFrame(stateTime);
            default:
                return img;
        }
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
    
    @Override
    public void update(float delta) {
        super.update(delta);
        stateTime += delta;
    }

    public String toString() {
        return "State: " + state + newline +
               "Position: " + position + newline +
               "Velocity: " + velocity + newline +
               "Acceleration: " + acceleration + newline;
    }
}
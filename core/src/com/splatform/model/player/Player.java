package com.splatform.model.player;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.splatform.model.world.AnimatedObject;
import com.splatform.view.WorldRenderer;

public class Player extends AnimatedObject {
    
    private static final String newline = System.getProperty("line.separator");

    private static final float FPS_SCL = 1/3f;

    /**
     * This enum is used to track the state of the player
     * The player will only ever be in one state at a time
     */
    public enum State {
        STANDING_LEFT,
        STANDING_RIGHT,
        RUNNING_LEFT,
        RUNNING_RIGHT,
        JUMPING_LEFT,
        JUMPING_RIGHT,
        FLYING_LEFT,
        FLYING_RIGHT,
        LANDING_LEFT,
        LANDING_RIGHT
    }
    
    // The current state that the player is in
    private State state;
    
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

    private Animation standLeftFrames;
    private Animation standRightFrames;
    private Animation runLeftFrames;
    private Animation runRightFrames;
    private Animation jumpLeftFrames;
    private Animation jumpRightFrames;
    private Animation flyLeftFrames;
    private Animation flyRightFrames;
    private Animation fallLeftFrames;
    private Animation fallRightFrames;
    private Animation landLeftFrames;
    private Animation landRightFrames;

    /**
     * Creates a new player at the provided position with the provided width and height
     * 
     * @param position The initial (x, y) position of the player
     * @param width The width of the player's bounding box
     * @param height The height of the player's bounding box
     */
    public Player(Vector2 position, float width, float height, State state) {
        super(position, width, height);
        this.state = state;
        standLeftFrames = new Animation(FPS_SCL, WorldRenderer.TEXTURES.createSprite("Dale"));
        standLeftFrames.setPlayMode(PlayMode.LOOP);
        animations.add(standLeftFrames);
        standRightFrames = new Animation(FPS_SCL, WorldRenderer.TEXTURES.createSprite("badlogic"));
        standRightFrames.setPlayMode(PlayMode.LOOP);
        animations.add(standRightFrames);
        runLeftFrames = new Animation(FPS_SCL, WorldRenderer.TEXTURES.createSprite("badlogic"),
                WorldRenderer.TEXTURES.createSprite("metal_plate"));
        runLeftFrames.setPlayMode(PlayMode.LOOP);
        animations.add(runLeftFrames);
        runRightFrames = new Animation(FPS_SCL, WorldRenderer.TEXTURES.createSprite("badlogic"),
                WorldRenderer.TEXTURES.createSprite("metal_plate"));
        runRightFrames.setPlayMode(PlayMode.LOOP);
        animations.add(runRightFrames);
        jumpLeftFrames = new Animation(FPS_SCL, WorldRenderer.TEXTURES.createSprite("badlogic"),
                WorldRenderer.TEXTURES.createSprite("metal_plate"));
        jumpLeftFrames.setPlayMode(PlayMode.NORMAL);
        animations.add(jumpLeftFrames);
        jumpRightFrames = new Animation(FPS_SCL, WorldRenderer.TEXTURES.createSprite("badlogic"),
                WorldRenderer.TEXTURES.createSprite("metal_plate"));
        jumpRightFrames.setPlayMode(PlayMode.NORMAL);
        animations.add(jumpRightFrames);
        flyLeftFrames = new Animation(FPS_SCL, WorldRenderer.TEXTURES.createSprite("badlogic"),
                WorldRenderer.TEXTURES.createSprite("metal_plate"));
        flyLeftFrames.setPlayMode(PlayMode.LOOP);
        animations.add(flyLeftFrames);
        flyRightFrames = new Animation(FPS_SCL, WorldRenderer.TEXTURES.createSprite("badlogic"),
                WorldRenderer.TEXTURES.createSprite("metal_plate"));
        flyRightFrames.setPlayMode(PlayMode.LOOP);
        animations.add(flyRightFrames);
        setFrameSize(bounds.width, bounds.height);
        fallLeftFrames = new Animation(FPS_SCL, WorldRenderer.TEXTURES.createSprite("badlogic"),
                WorldRenderer.TEXTURES.createSprite("metal_plate"));
        fallLeftFrames.setPlayMode(PlayMode.LOOP);
        animations.add(fallLeftFrames);
        fallRightFrames = new Animation(FPS_SCL, WorldRenderer.TEXTURES.createSprite("badlogic"),
                WorldRenderer.TEXTURES.createSprite("metal_plate"));
        fallRightFrames.setPlayMode(PlayMode.LOOP);
        animations.add(fallRightFrames);
        setFrameSize(bounds.width, bounds.height);
        landLeftFrames = new Animation(FPS_SCL, WorldRenderer.TEXTURES.createSprite("badlogic"),
                WorldRenderer.TEXTURES.createSprite("metal_plate"));
        landLeftFrames.setPlayMode(PlayMode.NORMAL);
        animations.add(landLeftFrames);
        landRightFrames = new Animation(FPS_SCL, WorldRenderer.TEXTURES.createSprite("badlogic"),
                WorldRenderer.TEXTURES.createSprite("metal_plate"));
        landRightFrames.setPlayMode(PlayMode.NORMAL);
        animations.add(landRightFrames);
        setFrameSize(bounds.width, bounds.height);
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
            case STANDING_LEFT:
                return standLeftFrames.getKeyFrame(stateTime);
            case STANDING_RIGHT:
                return standRightFrames.getKeyFrame(stateTime);
            case RUNNING_LEFT:
                return runLeftFrames.getKeyFrame(stateTime);
            case RUNNING_RIGHT:
                return runRightFrames.getKeyFrame(stateTime);
            case JUMPING_LEFT:
                return jumpLeftFrames.getKeyFrame(stateTime);
            case JUMPING_RIGHT:
                return jumpRightFrames.getKeyFrame(stateTime);
            case FLYING_LEFT:
                return flyLeftFrames.getKeyFrame(stateTime);
            case FLYING_RIGHT:
                return flyRightFrames.getKeyFrame(stateTime);
            default:
                throw new IllegalStateException("Player is in an illegal state.");
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
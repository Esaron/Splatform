package com.splatform.controller;

import com.badlogic.gdx.math.Vector2;
import com.splatform.model.player.Player;
import com.splatform.model.player.Player.State;
import com.splatform.view.WorldRenderer;

public class PlayerController {
    
    private static final float GRAVITY = -30f;
    
    private Player player = WorldRenderer.getInstance().getPlayer();
    private boolean leftHeld;
    private boolean rightHeld;
    private boolean jumpFlyHeld;
    
    private boolean isJumping;
    private boolean isFlying;
    
    private long jumpStartTime;
    private long flyStartTime;
    
    public PlayerController() {}
    
    public void leftPressed() {
        leftHeld = true;
    }
    
    public void rightPressed() {
        rightHeld = true;
    }
    
    public void jumpFlyPressed() {
        if (!isJumping) {
            jumpStartTime = System.currentTimeMillis();
            isJumping = true;
        }
        else if (!isFlying) {
            flyStartTime = System.currentTimeMillis();
            isFlying = true;
        }
        jumpFlyHeld = true;
    }
    
    public void leftReleased() {
        leftHeld = false;
    }
    
    public void rightReleased() {
        rightHeld = false;
    }
    
    public void jumpFlyReleased() {
        jumpFlyHeld = false;
    }
    
    public void update(float delta) {
        processInput();
        player.getAcceleration().y = GRAVITY;
        Vector2 velocity = player.getVelocity();
        Vector2 acceleration = player.getAcceleration();
        // velocity = initial velocity + acceleration * time
        velocity.add(acceleration.cpy().scl(delta));
        // position = initial position + velocity * time
        player.getPosition().add(velocity);
        
        if (player.getPosition().y < 0) {
            player.getPosition().y = 0;
            player.getVelocity().y = 0;
            player.getAcceleration().y = 0;
            if (isJumping) {
                isJumping = false;
            }
            if (isFlying) {
                isFlying = false;
            }
            if (player.getState().equals(State.JUMPING)
                    || player.getState().equals(State.FLYING)) {
                player.setState(State.STANDING);
            }
        }
        if (player.getPosition().x < 0) {
            player.getPosition().x = 0;
            player.getVelocity().x = 0;
            player.getAcceleration().x = 0;
            if (player.getState().equals(State.RUNNING)) {
                player.setState(State.STANDING);
            }
        }
        if (player.getPosition().x > WorldRenderer.WIDTH - player.getBounds().width) {
            player.getPosition().x = WorldRenderer.WIDTH - player.getBounds().width;
            player.getVelocity().x = 0;
            player.getAcceleration().x = 0;
            if (player.getState().equals(State.RUNNING)) {
                player.setState(State.STANDING);
            }
        }
        player.updateBounds();
    }
    
    private void moveLeft() {
        if (player.getState().equals(State.STANDING)) {
            player.setState(State.RUNNING);
        }
        player.getVelocity().x = -player.getRunVelocity();
    }
    
    private void moveRight() {
        if (player.getState().equals(State.STANDING)) {
            player.setState(State.RUNNING);
        }
        player.getVelocity().x = player.getRunVelocity();
    }
    
    private void stopMoving() {
        if (player.getState().equals(State.RUNNING)) {
            player.setState(State.STANDING);
        }
        player.getVelocity().x = 0;
    }
    
    private void jump() {
        player.setState(State.JUMPING);
        player.getVelocity().y = player.getJumpVelocity();
    }
    
    private void fly() {
        player.setState(State.FLYING);
        player.getVelocity().y = player.getFlyVelocity();
    }
    
    private void processInput() {
        if (jumpFlyHeld) {
            if (isJumping && System.currentTimeMillis() - jumpStartTime <= player.getMaxJumpTime()) {
                jump();
            }
            else if (isFlying && System.currentTimeMillis() - flyStartTime <= player.getMaxFlyTime()) {
                fly();
            }
            else {
                jumpFlyHeld = false;
            }
        }
        if (leftHeld) {
            moveLeft();
        }
        else if (rightHeld) {
            moveRight();
        }
        else {
            stopMoving();
        }
    }
}
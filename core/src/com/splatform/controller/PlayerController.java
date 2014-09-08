package com.splatform.controller;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.splatform.model.player.Player;
import com.splatform.model.player.Player.State;
import com.splatform.model.world.Platform;
import com.splatform.model.world.World;
import com.splatform.view.WorldRenderer;

public class PlayerController {
    
    private static final float GRAVITY = -30f;

    private World world = WorldRenderer.getInstance().getWorld();
    private Player player = world.getPlayer();
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
        Vector2 position = player.getPosition();
        Vector2 velocity = player.getVelocity();
        Vector2 acceleration = player.getAcceleration();
        Rectangle bounds = player.getBounds();
        acceleration.y = GRAVITY;
        // velocity = initial velocity + acceleration * time
        velocity.add(acceleration.cpy().scl(delta));
        // position = initial position + velocity * time
        position.add(velocity);
        
        if (position.y < 0) {
            position.y = 0;
            velocity.y = 0;
            acceleration.y = 0;
            land();
        }
        if (position.x < 0) {
            position.x = 0;
            velocity.x = 0;
            acceleration.x = 0;
            if (player.getState().equals(State.RUNNING)) {
                player.setState(State.STANDING);
            }
        }
        if (position.x > WorldRenderer.WIDTH - bounds.width) {
            position.x = WorldRenderer.WIDTH - bounds.width;
            velocity.x = 0;
            acceleration.x = 0;
            if (player.getState().equals(State.RUNNING)) {
                player.setState(State.STANDING);
            }
        }
        for (Platform platform : world.getPlatforms()) {
            Vector2 platformPosition = platform.getPosition();
            Rectangle platformBounds = platform.getBounds();
            if (platformBounds.overlaps(bounds)) {
                float rightToLeft = Math.abs((position.x + bounds.width) - platformPosition.x);
                float topToBottom = Math.abs((position.y + bounds.height) - platformPosition.y);
                float leftToRight = Math.abs(position.x - (platformPosition.x + platformBounds.width));
                float bottomToTop = Math.abs(position.y - (platformPosition.y + platformBounds.height));

                boolean closerToLeft = rightToLeft <= leftToRight;
                boolean closerToBottom = topToBottom <= bottomToTop;

                // Closest to bottom left corner
                if (closerToLeft && closerToBottom) {
                    // Closer to left
                    if (rightToLeft < topToBottom) {
                        position.x = platformBounds.x - bounds.width;
                        velocity.x = 0;
                        acceleration.x = 0;
                    }
                    // Closer to bottom
                    else {
                        position.y = platformBounds.y - bounds.height;
                        velocity.y = 0;
                    }
                }
                // Closest to top left corner
                else if (closerToLeft && !closerToBottom) {
                    // Closer to left
                    if (rightToLeft < bottomToTop) {
                        position.x = platformBounds.x - bounds.width;
                        velocity.x = 0;
                        acceleration.x = 0;
                    }
                    // Closer to top
                    else {
                        position.y = platformBounds.y + platformBounds.height;
                        land();
                    }
                }
                // Closest to bottom right corner
                else if (!closerToLeft && closerToBottom) {
                    // Closer to right
                    if (leftToRight < topToBottom) {
                        position.x = platformBounds.x + platformBounds.width;
                        velocity.x = 0;
                        acceleration.x = 0;
                    }
                    // Closer to bottom
                    else {
                        position.y = platformBounds.y - bounds.height;
                        velocity.y = 0;
                    }
                }
                // Closest to top right corner
                else {
                    // Closer to right
                    if (leftToRight < bottomToTop) {
                        position.x = platformBounds.x + platformBounds.width;
                        velocity.x = 0;
                        acceleration.x = 0;
                    }
                    // Closer to top
                    else {
                        position.y = platformBounds.y + platformBounds.height;
                        land();
                    }
                }
            }
        }
        player.update();
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
    
    private void stopRunning() {
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
    
    private void land() {
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
            stopRunning();
        }
    }
}
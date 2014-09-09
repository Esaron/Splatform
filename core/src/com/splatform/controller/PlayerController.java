package com.splatform.controller;

import com.badlogic.gdx.math.Intersector;
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
        // Update player according to input from user
        processInput();

        Vector2 playerPosition = player.getPosition();
        Vector2 playerOldPosition = playerPosition.cpy();
        Vector2 playerVelocity = player.getVelocity();
        Vector2 playerAcceleration = player.getAcceleration();
        Rectangle playerBounds = player.getBounds();

        // Get player center before moving
        Vector2 playerOldCenter = new Vector2();
        playerBounds.getCenter(playerOldCenter);

        // acceleration due to gravity
        playerAcceleration.y = GRAVITY;
        // velocity = initial velocity + acceleration * time
        playerVelocity.add(playerAcceleration.cpy().scl(delta));
        // position = initial position + velocity * time
        Vector2 playerCenter = playerPosition.add(playerVelocity).cpy().add(playerBounds.width/2, playerBounds.height/2);

        // Find the corners of the old and new sprite positions to use for our movement line
        Vector2 slope = playerCenter.cpy().sub(playerOldCenter);
        Vector2 moveStart;
        Vector2 moveEnd;
        // Get the rectangle surrounding the movement
        Rectangle moveBox = new Rectangle();
        // Get the distance moved
        Vector2 moveDistance;
        // bottom left to top right
        if (slope.x >= 0 && slope.y >= 0) {
            moveStart = playerOldPosition;
            moveEnd = playerPosition.cpy().add(playerBounds.width, playerBounds.height);
            moveDistance = moveEnd.cpy().sub(moveStart);
            moveBox.setPosition(moveStart);
        }
        // bottom right to top left
        else if (slope.x < 0 && slope.y < 0) {
            moveStart = playerOldPosition.cpy().add(playerBounds.width, 0);
            moveEnd = playerPosition.cpy().add(0, playerBounds.height);
            moveDistance = moveEnd.cpy().sub(moveStart);
            moveBox.setPosition(moveStart.cpy().add(moveDistance.x, 0));
        }
        // top left to bottom right
        else if (slope.x >= 0 && slope.y < 0) {
            moveStart = playerOldPosition.cpy().add(0, playerBounds.height);
            moveEnd = playerPosition.cpy().add(playerBounds.width, 0);
            moveDistance = moveEnd.cpy().sub(moveStart);
            moveBox.setPosition(moveStart.cpy().sub(moveDistance.y, 0));
        }
        // top right to bottom left
        else {
            moveStart = playerOldPosition.cpy().add(playerBounds.width, playerBounds.height);
            moveEnd = playerPosition;
            moveDistance = moveEnd.cpy().sub(moveStart);
            moveBox.setPosition(moveStart.cpy().add(moveDistance));
        }
        moveBox.setWidth(Math.abs(moveDistance.x));
        moveBox.setHeight(Math.abs(moveDistance.y));

        // The player is below the bottom of the screen
        // Set position to the ground and stop moving in the y direction
        if (playerPosition.y < 0) {
            playerPosition.y = 0;
            land();
        }
        // The player is to the left of the left of the screen
        // Set position to far left and stop moving in the x direction
        if (playerPosition.x < 0) {
            playerPosition.x = 0;
            stopMovingX();
        }
        // The player is to the right of the right of the screen
        // Set position to far right and stop moving in the x direction
        if (playerPosition.x > WorldRenderer.WIDTH - playerBounds.width) {
            playerPosition.x = WorldRenderer.WIDTH - playerBounds.width;
            stopMovingX();
        }
        // Check to see if the player is on a path to collide with any platform
        for (Platform platform : world.getPlatforms()) {
            Rectangle platformBounds = platform.getBounds();
            if (platformBounds.overlaps(moveBox)) {
                // Get all four corners to check for intersection with the movement line
                Vector2 bottomLeft = new Vector2(platformBounds.x, platformBounds.y);
                Vector2 topLeft = new Vector2(platformBounds.x,
                        platformBounds.y + platformBounds.height);
                Vector2 topRight = new Vector2(platformBounds.x + platformBounds.width,
                        platformBounds.y + platformBounds.height);
                Vector2 bottomRight = new Vector2(platformBounds.x + platformBounds.width,
                        platformBounds.y);
                Vector2 intersect = new Vector2();
                if (Intersector.intersectSegments(moveStart, moveEnd, topLeft, topRight, intersect)) {
                    playerPosition.y = topLeft.y;
                    land();
                }
                if (Intersector.intersectSegments(moveStart, moveEnd, bottomRight, bottomLeft, intersect)) {
                    playerPosition.y = bottomRight.y - playerBounds.height;
                    stopMovingY();
                }
                if (Intersector.intersectSegments(moveStart, moveEnd, bottomLeft, topLeft, intersect)) {
                    playerPosition.x = bottomLeft.x - playerBounds.width;
                    stopMovingX();
                }
                if (Intersector.intersectSegments(moveStart, moveEnd, topRight, bottomRight, intersect)) {
                    playerPosition.x = topRight.x;
                    stopMovingX();
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
    
    private void stopMovingX() {
        if (player.getState().equals(State.RUNNING)) {
            player.setState(State.STANDING);
        }
        player.getVelocity().x = 0;
    }
    
    private void stopMovingY() {
        if (player.getState().equals(State.JUMPING)
                || player.getState().equals(State.FLYING)) {
            player.setState(State.STANDING);
        }
        player.getVelocity().y = 0;
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
        stopMovingY();
        if (isJumping) {
            isJumping = false;
        }
        if (isFlying) {
            isFlying = false;
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
            stopMovingX();
        }
    }

    /**
     * Checks to see if a point falls outside the rectangle formed by using the start and end as points in one of its diagonals
     *
     * @param point
     * @param start
     * @param end
     * @return whether the point falls outside the rectangle or not
     */
    private boolean isPointOutsideSegmentBox(Vector2 point, Vector2 start, Vector2 end) {
        return (point.x >= start.x && point.x >= end.x
            ||  point.x <= start.x && point.x <= end.x)
            && (point.y >= start.y && point.y >= end.y
            ||  point.y <= start.y && point.y <= end.y);
    }

    /**
     * Returns the intersection of the ray beginning at the start point and a rectangle after passing through the center of the rectangle
     *
     * @param start The start of the ray
     * @param center The center of the rectangle
     * @param bottomLeft The bottom left of the rectangle (Not strictly necessary, but we'll have it; we can avoid divisions)
     * @param width The width of the rectangle
     * @param height The height of the rectangle
     * @return The intersection
     */
    private Vector2 findExtendedIntersection(Vector2 start, Vector2 center, Vector2 bottomLeft, float width, float height) {
        // Get remaining three player corners (position is bottom left)
        Vector2 topLeft = new Vector2(bottomLeft.x, bottomLeft.y + height);
        Vector2 topRight = new Vector2(bottomLeft.x + width, bottomLeft.y + height);
        Vector2 bottomRight = new Vector2(bottomLeft.x + width, bottomLeft.y);
        Vector2 end = new Vector2();
        // Check for intersection with top
        if (Intersector.intersectLines(start, center, topLeft, topRight, end)
                && isPointOutsideSegmentBox(end, bottomLeft, topRight)) {
            return end;
        }
        // Check bottom
        else if (Intersector.intersectLines(start, center, bottomRight, bottomLeft, end)
                && isPointOutsideSegmentBox(end, bottomLeft, topRight)) {
            return end;
        }
        // Check right
        else if (Intersector.intersectLines(start, center, topRight, bottomRight, end)
                && isPointOutsideSegmentBox(end, bottomLeft, topRight)) {
            return end;
        }
        // Must be left, but we want intersection point
        else if (Intersector.intersectLines(start, center, bottomLeft, topLeft, end)
                && isPointOutsideSegmentBox(end, bottomLeft, topRight)) {
            return end;
        }
        else {
            // Shouldn't happen
            throw new RuntimeException("The movement line doesn't intersect the player. This should never happen.");
        }
    }
}
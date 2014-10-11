package com.splatform.controller;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.splatform.model.player.Player;
import com.splatform.model.player.Player.State;
import com.splatform.model.world.Platform;
import com.splatform.model.world.World;
import com.splatform.view.WorldRenderer;

public class PlayerController {

    private enum MotionType {
        UP,
        DOWN,
        LEFT,
        RIGHT,
        LEFT_UP,
        LEFT_DOWN,
        RIGHT_UP,
        RIGHT_DOWN
    }

    private static final float GRAVITY = -1900f;

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

        // acceleration due to gravity
        if (!(player.getState().equals(State.STANDING_LEFT)
                || player.getState().equals(State.STANDING_RIGHT))) {
            playerAcceleration.y = GRAVITY;
        }
        // velocity = initial velocity + acceleration * time
        playerVelocity.add(playerAcceleration.cpy().scl(delta));
        // position = initial position + velocity * time
        playerPosition.add(playerVelocity.cpy().scl(delta));

        // Store the corners of the old and new positions

        // Find the corners of the old and new sprite positions to use for our movement line
        Vector2 slope = playerPosition.cpy().sub(playerOldPosition);
        if (slope.x != 0 || slope.y != 0) {
            // Get the rectangle surrounding the movement
            Rectangle moveBox;
            // Get the start and end points for the segments connecting the start and end of the
            // leading three corners in the direction of movement
            Vector2 midStart = new Vector2();
            Vector2 midEnd = new Vector2();
            Vector2 leftStart = new Vector2();
            Vector2 leftEnd = new Vector2();
            Vector2 rightStart = new Vector2();
            Vector2 rightEnd = new Vector2();
            MotionType motionType;
            if (slope.x == 0) {
                // up
                if (slope.y > 0) {
                    motionType = MotionType.UP;

                    // Move box dimensions and position
                    moveBox = new Rectangle(playerOldPosition.x, playerOldPosition.y,
                            playerBounds.width, playerPosition.y - playerOldPosition.y + playerBounds.height);
                }
                // down
                else {
                    motionType = MotionType.DOWN;

                    moveBox = new Rectangle(playerPosition.x, playerPosition.y,
                            playerBounds.width, playerOldPosition.y - playerPosition.y + playerBounds.height);
                }
            }
            else if (slope.y == 0) {
                // right
                if (slope.x > 0) {
                    motionType = MotionType.RIGHT;

                    moveBox = new Rectangle(playerOldPosition.x, playerOldPosition.y,
                            playerPosition.x - playerOldPosition.x + playerBounds.width, playerBounds.height);
                }
                // left
                else {
                    motionType = MotionType.LEFT;

                    moveBox = new Rectangle(playerPosition.x, playerPosition.y,
                            playerOldPosition.x - playerPosition.x + playerBounds.width, playerBounds.height);
                }
            }
            // bottom left to top right
            else if (slope.x > 0 && slope.y > 0) {
                motionType = MotionType.RIGHT_UP;

                moveBox = new Rectangle(playerPosition.x - slope.x, playerPosition.y - slope.y,
                        slope.x + playerBounds.width, slope.y + playerBounds.height);

                midStart = playerOldPosition;
                midEnd = playerPosition.cpy().add(playerBounds.width, playerBounds.height);
                leftStart.x = playerOldPosition.x;
                leftStart.y = playerOldPosition.y + playerBounds.height;
                leftEnd = leftStart.cpy().add(slope);
                rightStart.x = playerOldPosition.x + playerBounds.width;
                rightStart.y = playerOldPosition.y;
                rightEnd = rightStart.cpy().add(slope);
            }
            // bottom right to top left
            else if (slope.x < 0 && slope.y > 0) {
                motionType = MotionType.LEFT_UP;

                moveBox = new Rectangle(playerPosition.x, playerPosition.y - slope.y,
                        -slope.x + playerBounds.width, slope.y + playerBounds.height);

                midStart = playerOldPosition.cpy().add(playerBounds.width, 0);
                midEnd = playerPosition.cpy().add(0, playerBounds.height);
                leftStart.x = playerOldPosition.x;
                leftStart.y = playerOldPosition.y;
                leftEnd = leftStart.cpy().add(slope);
                rightStart.x = playerOldPosition.x + playerBounds.width;
                rightStart.y = playerOldPosition.y + playerBounds.height;
                rightEnd = rightStart.cpy().add(slope);
            }
            // top left to bottom right
            else if (slope.x > 0 && slope.y < 0) {
                motionType = MotionType.RIGHT_DOWN;

                moveBox = new Rectangle(playerOldPosition.x, playerOldPosition.y + slope.y,
                        slope.x + playerBounds.width, -slope.y + playerBounds.height);

                midStart = playerOldPosition.cpy().add(0, playerBounds.height);
                midEnd = playerPosition.cpy().add(playerBounds.width, 0);
                leftStart.x = playerOldPosition.x;
                leftStart.y = playerOldPosition.y;
                leftEnd = leftStart.cpy().add(slope);
                rightStart.x = playerOldPosition.x + playerBounds.width;
                rightStart.y = playerOldPosition.y + playerBounds.height;
                rightEnd = rightStart.cpy().add(slope);
            }
            // top right to bottom left
            else {
                motionType = MotionType.LEFT_DOWN;

                moveBox = new Rectangle(playerPosition.x, playerPosition.y,
                        -slope.x + playerBounds.width, -slope.y + playerBounds.height);

                midStart = playerOldPosition.cpy().add(playerBounds.width, playerBounds.height);
                midEnd = playerPosition;
                leftStart.x = playerOldPosition.x;
                leftStart.y = playerOldPosition.y + playerBounds.height;
                leftEnd = leftStart.cpy().add(slope);
                rightStart.x = playerOldPosition.x + playerBounds.width;
                rightStart.y = playerOldPosition.y;
                rightEnd = rightStart.cpy().add(slope);
            }

            // Create the arrays representing the parallelogram paths taken by the two edge segments
            // of the player facing in the direction of movement
            Array<Vector2> leftRegion = new Array<Vector2>(new Vector2[]{midStart,
                    midEnd, leftEnd, leftStart});
            Array<Vector2> rightRegion = new Array<Vector2>(new Vector2[]{midStart,
                    midEnd, rightEnd, rightStart});
            // Check to see if the player is on a path to collide with any platform
            // Keep track of the closest platform and only collide with that one
            float closestDistance = Float.MAX_VALUE;
            Vector2 platformIntersect = new Vector2();
            // Temp variable to store the actual movement correction
            Vector2 correctedPosition = playerPosition.cpy();
            for (Platform platform : world.getPlatforms()) {
                Rectangle platformBounds = platform.getBounds();
                Rectangle intersection = new Rectangle();
                if (Intersector.intersectRectangles(platformBounds, moveBox, intersection)) {
                    Vector2 intersectPosition = new Vector2();
                    intersection.getPosition(intersectPosition);
                    Vector2 bottomRight = intersectPosition.cpy().add(intersection.width, 0);
                    Vector2 topLeft = intersectPosition.cpy().add(0, intersection.height);
                    Vector2 topRight = intersectPosition.cpy().add(intersection.width, intersection.height);
                    boolean hitY = false;
                    boolean hitX = false;
                    float deltaY;
                    float deltaX;
                    float distance;
                    switch(motionType) {
                        case UP:
                            distance = platformBounds.y - playerOldPosition.y + playerBounds.height;
                            if (distance < closestDistance) {
                                closestDistance = distance;
                                correctedPosition.y = platformBounds.y - playerBounds.height;
                            }
                            break;
                        case DOWN:
                            distance = playerOldPosition.y - platformBounds.y + platformBounds.height;
                            if (distance < closestDistance) {
                                correctedPosition.y = platformBounds.y + platformBounds.height;
                            }
                            break;
                        case LEFT:
                            distance = playerOldPosition.x - platformBounds.x + platformBounds.width;
                            if (distance < closestDistance) {
                                correctedPosition.x = platformBounds.x + platformBounds.width;
                            }
                            break;
                        case RIGHT:
                            distance = platformBounds.x - playerOldPosition.x + playerBounds.width;
                            if (distance < closestDistance) {
                                correctedPosition.x = platformBounds.x - playerBounds.width;
                            }
                            break;
                        case LEFT_UP:
                            // Check if we hit the bottom of the platform and that the platform is the closest one
                            if (Intersector.intersectSegments(midStart, midEnd,
                                    intersectPosition, bottomRight, platformIntersect)) {
                                distance = platformIntersect.dst(midStart);
                                if (distance < closestDistance) {
                                    closestDistance = distance;
                                    hitY = true;
                                }
                            }
                            else if (Intersector.intersectSegments(rightStart, rightEnd,
                                    intersectPosition, bottomRight, platformIntersect)) {
                                distance = platformIntersect.dst(rightStart);
                                if (distance < closestDistance) {
                                    closestDistance = distance;
                                    hitY = true;
                                }
                            }
                            else if (Intersector.isPointInPolygon(rightRegion, intersectPosition)) {
                                deltaY = bottomRight.y - playerOldPosition.y + playerBounds.height;
                                deltaX = slope.x/slope.y*deltaY;
                                distance = (float) getHypotenuse(deltaX, deltaY);
                                if (distance < closestDistance) {
                                    closestDistance = distance;
                                    hitY = true;
                                }
                            }

                            // Check if we hit the right of the platform
                            else if (Intersector.intersectSegments(midStart, midEnd,
                                    bottomRight, topRight, platformIntersect)) {
                                distance = platformIntersect.dst(midStart);
                                if (distance < closestDistance) {
                                    closestDistance = distance;
                                    hitX = true;
                                }
                            }
                            else if (Intersector.intersectSegments(leftStart, leftEnd,
                                    bottomRight, topRight, platformIntersect)) {
                                distance = platformIntersect.dst(leftStart);
                                if (distance < closestDistance) {
                                    closestDistance = distance;
                                    hitX = true;
                                }
                            }
                            else if (Intersector.isPointInPolygon(leftRegion, topRight)) {
                                deltaY = bottomRight.y - playerOldPosition.y + playerBounds.height;
                                deltaX = slope.x/slope.y*deltaY;
                                distance = (float) getHypotenuse(deltaX, deltaY);
                                if (distance < closestDistance) {
                                    closestDistance = distance;
                                    hitX = true;
                                }
                            }

                            // Adjust position and motion if we collided
                            if (hitY) {
                                correctedPosition.y = platformBounds.y - playerBounds.height;
                            }
                            if (hitX) {
                                correctedPosition.x = platformBounds.x + platformBounds.width;
                            }
                            break;
                        case RIGHT_UP:
                            // Check for collision with bottom
                            if (Intersector.intersectSegments(midStart, midEnd,
                                        intersectPosition, bottomRight, platformIntersect)) {
                                distance = platformIntersect.dst(midStart);
                                if (distance < closestDistance) {
                                    closestDistance = distance;
                                    hitY = true;
                                }
                            }
                            else if (Intersector.intersectSegments(leftStart, leftEnd,
                                        intersectPosition, topLeft, platformIntersect)) {
                                distance = platformIntersect.dst(leftStart);
                                if (distance < closestDistance) {
                                    closestDistance = distance;
                                    hitY = true;
                                }
                            }
                            else if (Intersector.isPointInPolygon(leftRegion, bottomRight)) {
                                deltaY = intersectPosition.y - playerOldPosition.y + playerBounds.height;
                                deltaX = slope.x/slope.y*deltaY;
                                distance = (float) getHypotenuse(deltaX, deltaY);
                                if (distance < closestDistance) {
                                    closestDistance = distance;
                                    hitY = true;
                                }
                            }

                            // Check for collision with left side
                            else if (Intersector.intersectSegments(midStart, midEnd,
                                        intersectPosition, topLeft, platformIntersect)) {
                                distance = platformIntersect.dst(midStart);
                                if (distance < closestDistance) {
                                    closestDistance = distance;
                                    hitX = true;
                                }
                            }
                            else if (Intersector.intersectSegments(rightStart, rightEnd,
                                        intersectPosition, topLeft, platformIntersect)) {
                                distance = platformIntersect.dst(rightStart);
                                if (distance < closestDistance) {
                                    closestDistance = distance;
                                    hitX = true;
                                }
                            }
                            else if (Intersector.isPointInPolygon(rightRegion, topLeft)) {
                                deltaY = intersectPosition.y - playerOldPosition.y + playerBounds.height;
                                deltaX = slope.x/slope.y*deltaY;
                                distance = (float) getHypotenuse(deltaX, deltaY);
                                if (distance < closestDistance) {
                                    closestDistance = distance;
                                    hitX = true;
                                }
                            }

                            if (hitY) {
                                correctedPosition.y = platformBounds.y - playerBounds.height;
                            }
                            if (hitX) {
                                correctedPosition.x = platformBounds.x - playerBounds.width;
                            }
                            break;
                        case LEFT_DOWN:
                            // Check for collision with top
                            if (Intersector.intersectSegments(midStart, midEnd,
                                        topLeft, topRight, platformIntersect)) {
                                distance = platformIntersect.dst(midStart);
                                if (distance < closestDistance) {
                                    closestDistance = distance;
                                    hitY = true;
                                }
                            }
                            else if (Intersector.intersectSegments(rightStart, rightEnd,
                                        topLeft, topRight, platformIntersect)) {
                                distance = platformIntersect.dst(rightStart);
                                if (distance < closestDistance) {
                                    closestDistance = distance;
                                    hitY = true;
                                }
                            }
                            else if (Intersector.isPointInPolygon(rightRegion, topLeft)) {
                                deltaY = playerOldPosition.y - topRight.y;
                                deltaX = slope.x/slope.y*deltaY;
                                distance = (float) getHypotenuse(deltaX, deltaY);
                                if (distance < closestDistance) {
                                    closestDistance = distance;
                                    hitY = true;
                                }
                            }

                            // Check for collision with right side
                            else if (Intersector.intersectSegments(midStart, midEnd,
                                        bottomRight, topRight, platformIntersect)) {
                                distance = platformIntersect.dst(midStart);
                                if (distance < closestDistance) {
                                    closestDistance = distance;
                                    hitX = true;
                                }
                            }
                            else if (Intersector.intersectSegments(leftStart, leftEnd,
                                        bottomRight, topRight, platformIntersect)) {
                                distance = platformIntersect.dst(leftStart);
                                if (distance < closestDistance) {
                                    closestDistance = distance;
                                    hitX = true;
                                }
                            }
                            else if (Intersector.isPointInPolygon(leftRegion, bottomRight)) {
                                deltaY = playerOldPosition.y - topRight.y;
                                deltaX = slope.x/slope.y*deltaY;
                                distance = (float) getHypotenuse(deltaX, deltaY);
                                if (distance < closestDistance) {
                                    closestDistance = distance;
                                    hitX = true;
                                }
                            }

                            if (hitY) {
                                correctedPosition.y = platformBounds.y + platformBounds.height;
                            }
                            if (hitX) {
                                correctedPosition.x = platformBounds.x + platformBounds.width;
                            }
                            break;
                        case RIGHT_DOWN:
                            // Check for collision with top
                            if (Intersector.intersectSegments(midStart, midEnd,
                                        topLeft, topRight, platformIntersect)) {
                                distance = platformIntersect.dst(midStart);
                                if (distance < closestDistance) {
                                    closestDistance = distance;
                                    hitY = true;
                                }
                            }
                            else if (Intersector.intersectSegments(leftStart, leftEnd,
                                        topLeft, topRight, platformIntersect)) {
                                distance = platformIntersect.dst(leftStart);
                                if (distance < closestDistance) {
                                    closestDistance = distance;
                                    hitY = true;
                                }
                            }
                            else if (Intersector.isPointInPolygon(leftRegion, topRight)) {
                                deltaY = playerOldPosition.y - topLeft.y;
                                deltaX = slope.x/slope.y*deltaY;
                                distance = (float) getHypotenuse(deltaX, deltaY);
                                if (distance < closestDistance) {
                                    closestDistance = distance;
                                    hitY = true;
                                }
                            }

                            // Check for collision with left side
                            else if (Intersector.intersectSegments(midStart, midEnd,
                                        intersectPosition, topLeft, platformIntersect)) {
                                distance = platformIntersect.dst(midStart);
                                if (distance < closestDistance) {
                                    closestDistance = distance;
                                    hitX = true;
                                }
                            }
                            else if (Intersector.intersectSegments(rightStart, rightEnd,
                                        intersectPosition, topLeft, platformIntersect)) {
                                distance = platformIntersect.dst(rightStart);
                                if (distance < closestDistance) {
                                    closestDistance = distance;
                                    hitX = true;
                                }
                            }
                            else if (Intersector.isPointInPolygon(rightRegion, intersectPosition)) {
                                deltaY = playerOldPosition.y - topLeft.y;
                                deltaX = slope.x/slope.y*deltaY;
                                distance = (float) getHypotenuse(deltaX, deltaY);
                                if (distance < closestDistance) {
                                    closestDistance = distance;
                                    hitX = true;
                                }
                            }

                            if (hitY) {
                                correctedPosition.y = platformBounds.y + platformBounds.height;
                            }
                            if (hitX) {
                                correctedPosition.x = platformBounds.x - playerBounds.width;
                            }
                            break;
                    }
                }
            }
            boolean stopX = playerPosition.x != correctedPosition.x;
            boolean stopUp = playerPosition.y > correctedPosition.y;
            boolean stopDown = playerPosition.y < correctedPosition.y;
            // Set the new position and alter the velocity
            playerPosition.set(correctedPosition);
            if (stopX) {
                stopMovingX();
            }
            else if (stopUp) {
                stopMovingY();
            }
            else if (stopDown) {
                land();
            }
        }

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

        player.update(delta);
    }
    
    private void moveLeft() {
        if (player.getState().equals(State.STANDING_LEFT)
                || player.getState().equals(State.STANDING_RIGHT)) {
            player.setState(State.RUNNING_LEFT);
        }
        player.getVelocity().x = -player.getRunVelocity();
    }
    
    private void moveRight() {
        if (player.getState().equals(State.STANDING_LEFT)
                || player.getState().equals(State.STANDING_RIGHT)) {
            player.setState(State.RUNNING_RIGHT);
        }
        player.getVelocity().x = player.getRunVelocity();
    }
    
    private void stopMovingX() {
        State state = player.getState();
        if (state.equals(State.RUNNING_LEFT)) {
            player.setState(State.STANDING_LEFT);
        }
        else if (state.equals(State.RUNNING_RIGHT)) {
            player.setState(State.STANDING_RIGHT);
        }
        player.getVelocity().x = 0;
    }
    
    private void stopMovingY() {
        player.getVelocity().y = 0;
    }
    
    private void jump() {
        State state = player.getState();
        if (state.equals(State.STANDING_LEFT)
                || state.equals(State.RUNNING_LEFT)) {
            player.setState(State.JUMPING_LEFT);
        }
        else if (state.equals(State.STANDING_RIGHT)
                || state.equals(State.RUNNING_RIGHT)) {
            player.setState(State.JUMPING_RIGHT);
        }
        player.getVelocity().y = player.getJumpVelocity();
    }
    
    private void fly() {
        State state = player.getState();
        if (state.equals(State.JUMPING_LEFT)) {
            player.setState(State.FLYING_LEFT);
        }
        else if (state.equals(State.JUMPING_RIGHT)) {
            player.setState(State.FLYING_RIGHT);
        }
        player.getVelocity().y = player.getFlyVelocity();
    }
    
    private void land() {
        if (player.getState().equals(State.JUMPING_LEFT)
                || player.getState().equals(State.FLYING_LEFT)) {
            player.setState(State.STANDING_LEFT);
        }
        else if (player.getState().equals(State.JUMPING_RIGHT)
                || player.getState().equals(State.FLYING_RIGHT)) {
            player.setState(State.STANDING_RIGHT);
        }
        stopMovingY();
        player.getAcceleration().y = 0;
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
    
    private double getHypotenuse(double deltaX, double deltaY) {
        return Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
    }
}
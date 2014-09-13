package com.splatform.controller;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
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

        // acceleration due to gravity
        if (!player.getState().equals(State.STANDING)) {
            playerAcceleration.y = GRAVITY;
        }
        // velocity = initial velocity + acceleration * time
        playerVelocity.add(playerAcceleration.cpy().scl(delta));
        // position = initial position + velocity * time
        playerPosition.add(playerVelocity);

        // Find the corners of the old and new sprite positions to use for our movement line
        Vector2 slope = playerPosition.cpy().sub(playerOldPosition);
        if (slope.x != 0 || slope.y != 0) {
            Vector2 moveStart = new Vector2();
            Vector2 moveEnd = new Vector2();
            // Get the rectangle surrounding the movement
            Rectangle moveBox = new Rectangle();
            // Get the distance moved
            Vector2 moveBoxWidthHeight;
            // Get the start and end points for the segments connecting the start and end of the
            // outside corners of the direction of movement
            Vector2 leftStart = new Vector2();
            Vector2 leftEnd = new Vector2();
            Vector2 rightStart = new Vector2();
            Vector2 rightEnd = new Vector2();
            MotionType motionType;
            if (slope.x == 0) {
                // up
                if (slope.y > 0) {
                    motionType = MotionType.UP;

                    // Move distance and move box
                    moveBoxWidthHeight = new Vector2(playerBounds.width,
                            playerPosition.y - playerOldPosition.y + playerBounds.height);
                    moveBox.setPosition(playerOldPosition);
                }
                // down
                else {
                    motionType = MotionType.DOWN;

                    moveBoxWidthHeight = new Vector2(playerBounds.width,
                            playerOldPosition.y - playerPosition.y + playerBounds.height);
                    moveBox.setPosition(playerPosition);
                }
            }
            else if (slope.y == 0) {
                // right
                if (slope.x > 0) {
                    motionType = MotionType.RIGHT;

                    moveBoxWidthHeight = new Vector2(playerPosition.x - playerOldPosition.x + playerBounds.width,
                            playerBounds.height);
                    moveBox.setPosition(playerOldPosition);
                }
                // left
                else {
                    motionType = MotionType.LEFT;

                    moveBoxWidthHeight = new Vector2(playerOldPosition.x - playerPosition.x + playerBounds.width,
                            playerBounds.height);
                    moveBox.setPosition(playerPosition);
                }
            }
            // bottom left to top right
            else if (slope.x > 0 && slope.y > 0) {
                motionType = MotionType.RIGHT_UP;

                moveStart = playerOldPosition;
                moveEnd = playerPosition.cpy().add(playerBounds.width, playerBounds.height);
                moveBoxWidthHeight = moveEnd.cpy().sub(moveStart);
                moveBox.setPosition(moveStart);

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

                moveStart = playerOldPosition.cpy().add(playerBounds.width, 0);
                moveEnd = playerPosition.cpy().add(0, playerBounds.height);
                moveBoxWidthHeight = moveEnd.cpy().sub(moveStart);
                moveBox.setPosition(moveStart.cpy().add(moveBoxWidthHeight.x, 0));

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

                moveStart = playerOldPosition.cpy().add(0, playerBounds.height);
                moveEnd = playerPosition.cpy().add(playerBounds.width, 0);
                moveBoxWidthHeight = moveEnd.cpy().sub(moveStart);
                moveBox.setPosition(moveStart.cpy().add(0, moveBoxWidthHeight.y));

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

                moveStart = playerOldPosition.cpy().add(playerBounds.width, playerBounds.height);
                moveEnd = playerPosition;
                moveBoxWidthHeight = moveEnd.cpy().sub(moveStart);
                moveBox.setPosition(moveStart.cpy().add(moveBoxWidthHeight));

                leftStart.x = playerOldPosition.x;
                leftStart.y = playerOldPosition.y + playerBounds.height;
                leftEnd = leftStart.cpy().add(slope);
                rightStart.x = playerOldPosition.x + playerBounds.width;
                rightStart.y = playerOldPosition.y;
                rightEnd = rightStart.cpy().add(slope);
            }
            moveBox.setWidth(Math.abs(moveBoxWidthHeight.x));
            moveBox.setHeight(Math.abs(moveBoxWidthHeight.y));

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
            float closestDistance = Float.MAX_VALUE;
            for (Platform platform : world.getPlatforms()) {
                Rectangle platformBounds = platform.getBounds();
                Rectangle intersection = new Rectangle();
                if (Intersector.intersectRectangles(platformBounds, moveBox, intersection)) {
                    Array<Vector2> leftRegion = new Array<Vector2>(new Vector2[]{moveStart,
                            moveEnd, leftEnd, leftStart});
                    Array<Vector2> rightRegion = new Array<Vector2>(new Vector2[]{moveStart,
                            moveEnd, rightEnd, rightStart});
                    Vector2 intersectPosition = new Vector2();
                    intersection.getPosition(intersectPosition);
                    Vector2 bottomRight = intersectPosition.cpy().add(intersection.width, 0);
                    Vector2 topLeft = intersectPosition.cpy().add(0, intersection.height);
                    Vector2 topRight = intersectPosition.cpy().add(intersection.width, intersection.height);
                    Vector2 platformIntersect = new Vector2();
                    switch(motionType) {
                        case UP:
                            playerPosition.y = platformBounds.y - playerBounds.height;
                            stopMovingY();
                            break;
                        case DOWN:
                            playerPosition.y = platformBounds.y + platformBounds.height;
                            land();
                            break;
                        case LEFT:
                            playerPosition.x = platformBounds.x + platformBounds.width;
                            stopMovingX();
                            break;
                        case RIGHT:
                            playerPosition.x = platformBounds.x - playerBounds.width;
                            stopMovingX();
                            break;
                        case LEFT_UP:
                            if (Intersector.intersectSegments(moveStart, moveEnd,
                                        intersectPosition, bottomRight, platformIntersect)
                                    || Intersector.intersectSegments(rightStart, rightEnd,
                                        intersectPosition, bottomRight, platformIntersect)
                                    || Intersector.isPointInPolygon(rightRegion, intersectPosition)) {
                                playerPosition.y = platformBounds.y - playerBounds.height;
                                stopMovingY();
                            }
                            else if (Intersector.intersectSegments(moveStart, moveEnd,
                                        bottomRight, topRight, platformIntersect)
                                    || Intersector.intersectSegments(leftStart, leftEnd,
                                        bottomRight, topRight, platformIntersect)
                                    || Intersector.isPointInPolygon(leftRegion, topRight)) {
                                playerPosition.x = platformBounds.x + platformBounds.width;
                                stopMovingX();
                            }
                            break;
                        case RIGHT_UP:
                            if (Intersector.intersectSegments(moveStart, moveEnd,
                                        intersectPosition, bottomRight, platformIntersect)
                                    || Intersector.intersectSegments(leftStart, leftEnd,
                                        intersectPosition, topLeft, platformIntersect)
                                    || Intersector.isPointInPolygon(leftRegion, bottomRight)) {
                                playerPosition.y = platformBounds.y - playerBounds.height;
                                stopMovingY();
                            }
                            else if (Intersector.intersectSegments(moveStart, moveEnd,
                                        intersectPosition, topLeft, platformIntersect)
                                    || Intersector.intersectSegments(rightStart, rightEnd,
                                        intersectPosition, topLeft, platformIntersect)
                                    || Intersector.isPointInPolygon(rightRegion, topLeft)) {
                                playerPosition.x = platformBounds.x - playerBounds.width;
                                stopMovingX();
                            }
                            break;
                        case LEFT_DOWN:
                            if (Intersector.intersectSegments(moveStart, moveEnd,
                                        topLeft, topRight, platformIntersect)
                                    || Intersector.intersectSegments(rightStart, rightEnd,
                                        topLeft, topRight, platformIntersect)
                                    || Intersector.isPointInPolygon(rightRegion, topLeft)) {
                                playerPosition.y = platformBounds.y + platformBounds.height;
                                land();
                            }
                            else if (Intersector.intersectSegments(moveStart, moveEnd,
                                        bottomRight, topRight, platformIntersect)
                                    || Intersector.intersectSegments(leftStart, leftEnd,
                                        bottomRight, topRight, platformIntersect)
                                    || Intersector.isPointInPolygon(leftRegion, bottomRight)) {
                                playerPosition.x = platformBounds.x + platformBounds.width;
                                stopMovingX();
                            }
                            break;
                        case RIGHT_DOWN:
                            if (Intersector.intersectSegments(moveStart, moveEnd,
                                        topLeft, topRight, platformIntersect)
                                    || Intersector.intersectSegments(leftStart, leftEnd,
                                        topLeft, topRight, platformIntersect)
                                    || Intersector.isPointInPolygon(leftRegion, topRight)) {
                                playerPosition.y = platformBounds.y + platformBounds.height;
                                land();
                            }
                            else if (Intersector.intersectSegments(moveStart, moveEnd,
                                        intersectPosition, topLeft, platformIntersect)
                                    || Intersector.intersectSegments(rightStart, rightEnd,
                                        intersectPosition, topLeft, platformIntersect)
                                    || Intersector.isPointInPolygon(rightRegion, intersectPosition)) {
                                playerPosition.x = platformBounds.x - playerBounds.width;
                                stopMovingX();
                            }
                            break;
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
    
    private void stopMovingX() {
        if (player.getState().equals(State.RUNNING)) {
            player.setState(State.STANDING);
        }
        player.getVelocity().x = 0;
    }
    
    private void stopMovingY() {
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
        if (player.getState().equals(State.JUMPING)
                || player.getState().equals(State.FLYING)) {
            player.setState(State.STANDING);
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
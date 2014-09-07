package com.splatform.model.level;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class LevelObject {

     // The default size (width/height) of a LevelObject
    protected static final float DEFAULT_SIZE = 0.5f;

     // The rectangular boundary surrounding a LevelObject
    protected Rectangle bounds = new Rectangle();

     // The (x, y) position of the LevelObject (usually its lower left corner)
    protected Vector2 position = new Vector2();

     // The velocity of the LevelObject in the x and y directions
    protected Vector2 velocity = new Vector2();

     // The acceleration of the LevelObject in the x and y directions
    protected Vector2 acceleration = new Vector2();

    /**
     * Creates a new LevelObject at the specified position with the provided size
     * 
     * @param position The initial (x, y) position of the LevelObject
     * @param width The initial width of the bounding box
     * @param height The initial height of the bounding box
     */
    public LevelObject(Vector2 position, float width, float height) {
        this.position = position;
        bounds.width = width;
        bounds.height = height;
        bounds.setPosition(position);
    }

    /**
     * Creates a new LevelObject at the specified position with the default size
     * 
     * @param position The initial (x, y) position of the LevelObject
     */
    public LevelObject(Vector2 position) {
        this(position, DEFAULT_SIZE, DEFAULT_SIZE);
    }
    
    public Vector2 getPosition() {
        return position;
    }
    
    public float getX() {
        return position.x;
    }
    
    public float getY() {
        return position.y;
    }
    
    public void setPosition(Vector2 position) {
        this.position = position;
        bounds.setPosition(position);
    }
    
    public Vector2 getVelocity() {
        return velocity;
    }
    
    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    public Vector2 getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(Vector2 acceleration) {
        this.acceleration = acceleration;
    }
    
    public Rectangle getBounds() {
        return bounds;
    }
    
    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }

    /**
     * Updates the bounding box according to the player position
     */
    public void updateBounds() {
        bounds.setPosition(position);
    }
}

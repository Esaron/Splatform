package com.splatform.controller;

import com.badlogic.gdx.math.Vector2;
import com.splatform.model.level.FallingObject;
import com.splatform.view.WorldRenderer;

public class FallingObjectController {
    private static final float GRAVITY = -30f;
    FallingObject fallingObject = WorldRenderer.getInstance().getFallingObject();
    
    public FallingObjectController() {
    }
    
    public void update(float delta) {
    	Vector2 velocity = fallingObject.getVelocity();
        Vector2 acceleration = fallingObject.getAcceleration();
    	fallingObject.getAcceleration().y = GRAVITY;
        velocity.add(acceleration.cpy().scl(delta));
        // position = initial position + velocity * time
        fallingObject.getPosition().add(velocity);
    	if(fallingObject.getX() == 0){
    		bounce();
    	}
    	fallingObject.updateBounds();
    }
    
    private void bounce() {
    	fallingObject.getAcceleration().y = 30;
    }
    
}

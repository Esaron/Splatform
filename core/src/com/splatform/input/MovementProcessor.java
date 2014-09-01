package com.splatform.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.splatform.player.Player;
import com.splatform.rendering.WorldRenderer;

public class MovementProcessor implements InputProcessor {
    private WorldRenderer renderer = WorldRenderer.getInstance();
    private boolean leftHeld;
    private boolean rightHeld;
    private boolean upHeld;
    private boolean downHeld;
    private boolean shiftHeld;

    public boolean isMovingLeft() {
        return leftHeld;
    }

    public boolean isMovingRight() {
        return rightHeld;
    }
    
    public boolean isMovingUp() {
    	return (upHeld && shiftHeld);
    }
    
    public boolean isMovingDown() {
    	return (downHeld && shiftHeld);
    }

    @Override
    public boolean keyDown(int keycode) {
        boolean result = false;
        Player player = renderer.getPlayer();
        switch(keycode) {
            case Input.Keys.A:
                leftHeld = true;
                result = true;
                break;
            case Input.Keys.D:
                rightHeld = true;
                result = true;
                break;
            case Input.Keys.SPACE:
                player.jump();
                result = true;
                break;
            case Input.Keys.W:
            	upHeld = true;
            	result = true;
            	break;
            case Input.Keys.S:
            	downHeld = true;
            	result = true;
            	break;
            case Input.Keys.SHIFT_LEFT:
            	shiftHeld = true;
            	result = true;
            	break;
        }
        return result;
    }

    @Override
    public boolean keyUp(int keycode) {
        boolean result = false;
        Player player = renderer.getPlayer();
        switch(keycode) {
            case Input.Keys.A:
                leftHeld = false;
                result = true;
                break;
            case Input.Keys.D:
                rightHeld = false;
                result = true;
                break;
            case Input.Keys.W:
            	upHeld = false;
            	result = true;
            	break;
            case Input.Keys.S:
            	downHeld = false;
            	result = true;
            	break;
            case Input.Keys.SHIFT_LEFT:
            	player.jump();
            	shiftHeld = false;
            	result = true;
            	break;
        }
        return result;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        boolean result = false;
        Player player = renderer.getPlayer();
        int sixthWidth = WorldRenderer.WIDTH/6;

        if (screenX < sixthWidth){
            leftHeld = true;
            result = true;
        }
        else if (screenX > WorldRenderer.WIDTH - sixthWidth){
            rightHeld = true;
            result = true;
        }
        else {
            player.jump();
            result = true;
        }
        return result;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        boolean result = false;
        int sixthWidth = WorldRenderer.WIDTH/6;

        if (screenX < sixthWidth){
            leftHeld = false;
            result = true;
        }
        else if (screenX > WorldRenderer.WIDTH - sixthWidth){
            rightHeld = false;
            result = true;
        }
        return result;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
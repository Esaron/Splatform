package com.splatform.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.splatform.controller.PlayerController;
import com.splatform.model.player.Player;
import com.splatform.model.player.Player.State;
import com.splatform.view.WorldRenderer;

public class MovementProcessor implements InputProcessor {
    
    PlayerController controller;
    
    public MovementProcessor(PlayerController controller) {
        this.controller = controller;
    }

    @Override
    public boolean keyDown(int keycode) {
        boolean result = false;
        switch(keycode) {
            case Input.Keys.A:
                controller.leftPressed();
                result = true;
                break;
            case Input.Keys.D:
                controller.rightPressed();
                result = true;
                break;
            case Input.Keys.SPACE:
                controller.jumpFlyPressed();
                result = true;
                break;
        }
        return result;
    }

    @Override
    public boolean keyUp(int keycode) {
        boolean result = false;
        switch(keycode) {
            case Input.Keys.A:
                controller.leftReleased();
                result = true;
                break;
            case Input.Keys.D:
                controller.rightReleased();
                result = true;
                break;
            case Input.Keys.SPACE:
                controller.jumpFlyReleased();
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
        int sixthWidth = WorldRenderer.WIDTH/6;

        if (screenX < sixthWidth) {
            controller.leftPressed();
            result = true;
        }
        else if (screenX > WorldRenderer.WIDTH - sixthWidth) {
            controller.rightPressed();
            result = true;
        }
        else {
            controller.jumpFlyPressed();
            result = true;
        }
        return result;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        boolean result = false;
        int sixthWidth = WorldRenderer.WIDTH/6;

        if (screenX < sixthWidth) {
            controller.leftReleased();
            result = true;
        }
        else if (screenX > WorldRenderer.WIDTH - sixthWidth) {
            controller.rightReleased();
            result = true;
        }
        else {
            controller.jumpFlyReleased();
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
package com.splatform.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.splatform.model.player.Player;
import com.splatform.view.WorldRenderer;

public class DebugProcessor implements InputProcessor {
    private WorldRenderer renderer = WorldRenderer.getInstance();

    @Override
    public boolean keyDown(int keycode) {
        boolean result = false;
        Player player = renderer.getPlayer();
        switch(keycode) {
            case Input.Keys.SLASH:
                System.out.println(player);
                result = true;
                break;
        }
        return result;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
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
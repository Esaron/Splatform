package com.splatform.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.splatform.player.Player;
import com.splatform.rendering.WorldRenderer;


public class MovementProcessor implements InputProcessor
{
    private WorldRenderer renderer = WorldRenderer.getInstance();

    @Override
    public boolean keyDown(int keycode)
    {
        boolean result = false;
        Player player = renderer.getPlayer();
        switch(keycode) {
            case Input.Keys.A:
                System.out.println("A pressed");
                player.setX(player.getX() - 5);
                result = true;
                break;
            case Input.Keys.D:
                System.out.println("D pressed");
                player.setX(player.getX() + 5);
                result = true;
                break;
        }
        return result;
    }

    @Override
    public boolean keyUp(int keycode)
    {
        boolean result = false;
        switch(keycode) {
            case Input.Keys.A:
                System.out.println("A released");
                result = true;
                break;
            case Input.Keys.D:
                System.out.println("D released");
                result = true;
                break;
        }
        return result;
    }

    @Override
    public boolean keyTyped(char character)
    {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button)
    {
    	boolean result = false;
        Player player = renderer.getPlayer();
        int halfWidth = renderer.WIDTH/2;
        
    	if(screenX < halfWidth){
    		player.setX(player.getX() - 5);
    		result = true;
    	}
    	else if(screenX >= halfWidth){
    		player.setX(player.getX() + 5);
    		result = true;
    	}
        return result;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button)
    {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer)
    {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY)
    {
        return false;
    }

    @Override
    public boolean scrolled(int amount)
    {
        return false;
    }
}

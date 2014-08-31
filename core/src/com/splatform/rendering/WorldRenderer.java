package com.splatform.rendering;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.splatform.player.Player;


public class WorldRenderer
{

    public final int WIDTH = Gdx.graphics.getWidth();
    public final int HEIGHT = Gdx.graphics.getHeight();
    private Player player;
    
    private static WorldRenderer renderer;
    
    private PerspectiveCamera cam;
    
    private WorldRenderer() {
    	player = new Player(0, 0);
    	player.setX((WIDTH - player.getWidth())/2);
    	player.setY((HEIGHT - player.getHeight())/2);
    }
    
    public static WorldRenderer getInstance() {
        if (renderer == null) {
            renderer = new WorldRenderer();
        }
        return renderer;
    }
    
    public PerspectiveCamera getCam() {
        return cam;
    }

    public Player getPlayer() {
        return player;
    }

    public void render(float delta, GL20 gl)
    {
    	player.render();
        this.cam.update(true);
    }
    
    public void resize(int width, int height) {
        WIDTH = width;
        HEIGHT = height;
        if (this.cam == null) {
            this.cam = new PerspectiveCamera(65, WIDTH, HEIGHT);
            this.cam.position.set(WIDTH/2, HEIGHT/2, 300);
            this.cam.near = 0.1f;
            this.cam.far = 300f;
            cam.update();
        }
    }
}

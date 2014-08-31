package com.splatform.rendering;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;
import com.splatform.character.Player;


public class WorldRenderer
{

    public static int WIDTH;
    public static int HEIGHT;
    private Player player;
    
    private static WorldRenderer renderer;
    
    private PerspectiveCamera cam;
    
    private WorldRenderer() {
    	player = new Player(WIDTH/2, HEIGHT/2);
    }
    
    public static WorldRenderer getInstance() {
        if (renderer == null) {
            renderer = new WorldRenderer();
        }
        return renderer;
    }
    
    public PerspectiveCamera getCam() {
        return this.cam;
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
            // Get the X axis relative to the camera
            Vector3 camRight = new Vector3().set(cam.direction).crs(cam.up);
            cam.update();
        }
    }
}

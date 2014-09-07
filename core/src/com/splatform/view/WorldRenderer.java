package com.splatform.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.splatform.model.player.Player;

public class WorldRenderer {

    // The game launches in 800x600
    public static int WIDTH = Gdx.graphics.getWidth();
    public static int HEIGHT = Gdx.graphics.getHeight();
    public static int CAMERA_WIDTH = WIDTH;
    public static int CAMERA_HEIGHT = HEIGHT;
    
    // Used to retrieve textures from the texture pack
    public static TextureAtlas TEXTURES = new TextureAtlas(Gdx.files.internal("textures/textures.pack"));
    
    // The singleton instance for the class
    private static WorldRenderer renderer;
    
    // The camera through which we see the scene
    private PerspectiveCamera cam;
    
    /*  Pixels per unit in the x and y directions
     *  All positions are in units
     *  All velocities are in units/second
     *  All accelerations are in units/second/second
     */
    private float ppux;
    private float ppuy;
    
    private Player player;
    
    // Used to draw all the sprites to the screen
    private SpriteBatch spriteBatch = new SpriteBatch();

    private WorldRenderer() {
        cam = new PerspectiveCamera(65, CAMERA_WIDTH, CAMERA_HEIGHT);
        cam.position.set(WIDTH/2, HEIGHT/2, 300);
        cam.near = 0.1f;
        cam.far = 300f;
        ppux = (float)WIDTH/CAMERA_WIDTH;
        ppuy = (float)HEIGHT/CAMERA_HEIGHT;
        player = new Player(new Vector2(), WIDTH/10f, HEIGHT/10f);
    }
    
    private float getPixelXValue(float unitXValue) {
        return unitXValue * ppux;
    }
    
    private float getPixelYValue(float unitYValue) {
        return unitYValue * ppuy;
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

    public void render(float delta, GL20 gl) {
        spriteBatch.begin();
        drawPlayer();
        spriteBatch.end();
    }
    
    private void drawPlayer() {
        spriteBatch.draw(player.getImg(),
                getPixelXValue(player.getX()),
                getPixelYValue(player.getY()),
                getPixelXValue(player.getWidth()),
                getPixelYValue(player.getHeight()));
    }

    public void resize(int width, int height) {
        WIDTH = width;
        HEIGHT = height;
        cam.position.set(WIDTH/2, HEIGHT/2, 300);
        cam.near = 0.1f;
        cam.far = 300f;
        player.setSize(WIDTH/10, HEIGHT/10);
        cam.update();
    }
}
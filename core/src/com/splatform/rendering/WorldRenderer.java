package com.splatform.rendering;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector2;
import com.splatform.input.MovementProcessor;
import com.splatform.player.Player;

public class WorldRenderer {

    public static int WIDTH = Gdx.graphics.getWidth();
    public static int HEIGHT = Gdx.graphics.getHeight();
    private static WorldRenderer renderer;
    private Player player;
    private PerspectiveCamera cam;
    private MovementProcessor processor;

    private WorldRenderer() {
        player = new Player(0, 0);
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

    public void setProcessor(MovementProcessor processor) {
        this.processor = processor;
    }

    public void render(float delta, GL20 gl) {
        int playerX = player.getX();
        int playerY = player.getY();
        int maxWidth = WIDTH - player.getWidth();
        int maxHeight = HEIGHT - player.getHeight();

        if (playerY < 0) {
            player.setY(0);
            player.setFalling(false);
        }
        else if (playerY > maxHeight) {
            player.setY(maxHeight);
        }

        if (playerX < 0) {
            player.setX(0);
        }
        else if (playerX > maxWidth) {
            player.setX(maxWidth);
        }

        if (player.isFalling()) {
            player.fall(delta);
        }
        player.accelerate(delta);

        if (processor.isMovingLeft()) {
            player.move(-player.getSpeed(), 0);
        }
        else if (processor.isMovingRight()) {
            player.move(player.getSpeed(), 0);
        }
        
        if (processor.isMovingUp()){
        	player.move(0, player.getSpeed());
        }
        else if (processor.isMovingDown()){
        	player.move(0, -player.getSpeed());
        }

        Vector2 playerVelocity = player.getVelocity();
        player.move((int)playerVelocity.x, (int)playerVelocity.y);
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
package com.splatform.model.world;

import com.badlogic.gdx.math.Vector2;
import com.splatform.view.WorldRenderer;

public class Platform extends NonAnimatedObject {

    private static float DEFAULT_WIDTH = WorldRenderer.WIDTH/10;
    private static float DEFAULT_HEIGHT = WorldRenderer.HEIGHT/30;

    public Platform(Vector2 position, float width, float height) {
        super(position, width, height);
        img = WorldRenderer.TEXTURES.createSprite("metal_plate");
        img.setSize(bounds.width, bounds.height);
    }

    public Platform(Vector2 position) {
        this(position, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }
}

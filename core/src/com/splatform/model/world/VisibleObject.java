package com.splatform.model.world;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public abstract class VisibleObject extends WorldObject {

    public VisibleObject(Vector2 position) {
        super(position);
    }

    public VisibleObject(Vector2 position, float width, float height) {
        super(position, width, height);
    }

    protected Sprite img;

    public Sprite getImg() {
        return img;
    }

    public void setImg(Sprite img) {
        img.setSize(bounds.width, bounds.height);
        this.img = img;
    }
}

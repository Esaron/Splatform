package com.splatform.model.world;

import java.util.ArrayList;
import java.util.List;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public abstract class AnimatedObject extends WorldObject {

    public AnimatedObject(Vector2 position) {
        super(position);
    }

    public AnimatedObject(Vector2 position, float width, float height) {
        super(position, width, height);
    }

    protected List<Animation> animations = new ArrayList<Animation>();;

    public List<Animation> getAnimations() {
        return animations;
    }

    public void setFrameSize(float width, float height) {
        for (Animation animation : animations) {
            for (TextureRegion frame : animation.getKeyFrames()) {
                ((Sprite)frame).setSize(bounds.width, bounds.height);
            }
        }
    }
}

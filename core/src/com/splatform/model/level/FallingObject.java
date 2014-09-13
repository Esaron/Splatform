package com.splatform.model.level;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.splatform.view.WorldRenderer;

public class FallingObject extends LevelObject{
	private Sprite img;
	
	public FallingObject(Vector2 position, float width, float height) {
		super(position, width, height);
		img = WorldRenderer.TEXTURES.createSprite(randomImage());
	}
	
	public String randomImage(){
		int rand = (int)(Math.random() * 2);
		String img = "anvil";
		switch (rand) {
			case 0:
				img = "moonstone0";
				break;
			case 1:
				img = "anvil";
				break;
			case 2:
				img = "piano";
				break;
		}
		return img;	
	}
	
	public Sprite getImg() {
		return img;
	}
	
	public float getWidth() {
		return bounds.width;
	}
	
	public float getHeight() {
		return bounds.height;
	}

}

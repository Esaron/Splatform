package com.splatform.model.level;

import java.util.Random;

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
		Random rand = new Random();
		String img = "anvil";
		switch (rand.nextInt(2)) {
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

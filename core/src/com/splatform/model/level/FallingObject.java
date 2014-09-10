package com.splatform.model.level;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.splatform.view.WorldRenderer;

public class FallingObject extends LevelObject{
	private Sprite img;
	
	public FallingObject(){
		super(new Vector2((int)(Math.random() * WorldRenderer.WIDTH), WorldRenderer.HEIGHT));
		img = WorldRenderer.TEXTURES.createSprite(randomImage());
	}
	
	public String randomImage(){
		int rand = (int)(Math.random() * 5);
		String img = " ";
		switch (rand) {
			case 0:
				img = " ";
			case 1:
				img = " ";
			case 2:
				img = " ";
			case 3:
				img = " ";
			case 4:
				img = " ";			
		}
		return img;
	}

}

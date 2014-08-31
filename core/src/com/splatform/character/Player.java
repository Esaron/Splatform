package com.splatform.character;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Player {
	private int x;
	private int y;
	private SpriteBatch body;
	private Texture img;

	public Player(int x, int y){
		this.x = x;
		this.y = y;
		body = new SpriteBatch();
		img = new Texture("badlogic.jpg");
	}

	public int getX(){
		return x;
	}

	public int getY(){
		return y;
	}
	
	public SpriteBatch getBody(){
		return body;
	}
	
	public Texture getImg(){
		return img;
	}

	public void setX(int x){
		this.x = x;
	}

	public void setY(int y){
		this.y = y;
	}
	
	public void setBody(SpriteBatch body){
		this.body = body;
	}
	
	public void setImg(Texture img){
		this.img = img;
	}
	
	public void render(){
		body.begin();
		body.draw(img, x, y);
		body.end();
	}
}

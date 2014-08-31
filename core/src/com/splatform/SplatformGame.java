package com.splatform;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.splatform.input.MovementProcessor;
import com.splatform.screens.GameScreen;

public class SplatformGame extends Game {

    private GameScreen screen;
    private InputMultiplexer plexer = new InputMultiplexer();
    
	@Override
	public void create () {
        this.screen = new GameScreen();
        this.setScreen(screen);
        plexer.addProcessor(new MovementProcessor());
        Gdx.input.setInputProcessor(plexer);
	}

	@Override
	public void render () {
		super.render();
	}
}

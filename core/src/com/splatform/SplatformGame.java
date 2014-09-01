package com.splatform;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.splatform.input.DebugProcessor;
import com.splatform.input.MovementProcessor;
import com.splatform.rendering.WorldRenderer;
import com.splatform.screens.GameScreen;

public class SplatformGame extends Game {

    private GameScreen screen;
    private InputMultiplexer plexer = new InputMultiplexer();

    @Override
    public void create () {
        this.screen = new GameScreen();
        this.setScreen(screen);
        MovementProcessor processor = new MovementProcessor();
        WorldRenderer.getInstance().setProcessor(processor);
        plexer.addProcessor(processor);
        plexer.addProcessor(new DebugProcessor());
        Gdx.input.setInputProcessor(plexer);
    }

    @Override
    public void render () {
        super.render();
    }
}
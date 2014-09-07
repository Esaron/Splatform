package com.splatform;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.splatform.input.DebugProcessor;
import com.splatform.input.MovementProcessor;
import com.splatform.screens.GameScreen;
import com.splatform.view.WorldRenderer;

public class SplatformGame extends Game {

    private GameScreen screen;

    @Override
    public void create() {
        this.screen = new GameScreen();
        this.setScreen(screen);
    }

    @Override
    public void render() {
        super.render();
    }
}
package com.splatform;

import com.badlogic.gdx.Game;
import com.splatform.screens.GameScreen;

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
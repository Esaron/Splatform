package com.splatform.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.splatform.controller.FallingObjectController;
import com.splatform.controller.PlayerController;
import com.splatform.input.DebugProcessor;
import com.splatform.input.MovementProcessor;
import com.splatform.view.WorldRenderer;

public class GameScreen implements Screen
{
    private InputMultiplexer plexer = new InputMultiplexer();
    private WorldRenderer renderer;
    private PlayerController controller;
    private FallingObjectController objectController; 

    public GameScreen() {}

    @Override
    public void render(float delta) {
        GL20 gl = Gdx.graphics.getGL20();
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        controller.update(delta);
        objectController.update(delta);
        renderer.render(delta, gl);
    }

    @Override
    public void resize(int width, int height) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        Gdx.gl.glDepthFunc(GL20.GL_LESS);
        //FIXME resize gets called twice...
        renderer.resize(width, height);
    }

    @Override
    public void show() {
        renderer = WorldRenderer.getInstance();
        objectController = new FallingObjectController();
        controller = new PlayerController();
        plexer.addProcessor(new MovementProcessor(controller));
        plexer.addProcessor(new DebugProcessor());
        Gdx.input.setInputProcessor(plexer);
    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
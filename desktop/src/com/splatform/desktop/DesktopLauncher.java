package com.splatform.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.splatform.SplatformGame;

public class DesktopLauncher {
    public static void main (String[] arg) {
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.width = 800;
        cfg.height = 600;
        new LwjglApplication(new SplatformGame(), "Game", 800, 600);
    }
}

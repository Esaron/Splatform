package com.splatform.model.world;

import java.util.List;
import java.util.ArrayList;
import com.splatform.model.player.Player;

public class World {
    private Player player;
    private List<Platform> platforms = new ArrayList<Platform>();

    public World() {}

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public List<Platform> getPlatforms() {
        return platforms;
    }

    public void update(float delta) {
        player.update(delta);
        for (Platform platform : platforms) {
            platform.update(delta);
        }
    }
}

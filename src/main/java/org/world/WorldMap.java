package org.world;

import javafx.scene.layout.HBox;

public class WorldMap extends GuiElement {
    World world;

    public WorldMap(HBox containerToBeUpdated, World world) {
        super(containerToBeUpdated);
        this.world = world;
    }

    @Override
    protected HBox newInstance() {
        return world.representation();
    }
}

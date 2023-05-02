package org.world;

import javafx.scene.layout.HBox;
import org.simulation.Simulation;

public class StartPause extends GuiElement {
    Simulation simulation;

    public StartPause(HBox containerToBeUpdated, Simulation simulation) {
        super(containerToBeUpdated);
        this.simulation = simulation;
    }

    @Override
    protected HBox newInstance() {
        return simulation.renderButtons();
    }
}

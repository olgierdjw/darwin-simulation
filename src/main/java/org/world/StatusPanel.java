package org.world;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.simulation.Simulation;

public class StatusPanel extends GuiElement {
    Simulation simulation;
    World world;

    public StatusPanel(HBox panelContainer, Simulation simulation, World world) {
        super(panelContainer);
        this.simulation = simulation;
        this.world = world;
    }

    @Override
    protected HBox newInstance() {
        int animalCount = world.animalCount();
        VBox infoPanel = new VBox(
                new Label("Day: " + simulation.getIterationNumber()),
                new Label("Animals: " + animalCount),
                new Label("Avg animal energy: " + world.energySum() / animalCount),
                new Label("Procreation threshold: " + world.minimumToProcreation()));
        return new HBox(infoPanel);
    }
}

package org.world;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class DroughtController extends GuiElement {

    World world;


    public DroughtController(HBox containerToBeUpdated, World world) {
        super(containerToBeUpdated);
        this.world = world;

    }

    boolean activated = false;

    @Override
    public HBox newInstance() {
        VBox draughtController = new VBox(new Label("Drought mode"));
        Button beforeDraught;
        if (!activated) {
            beforeDraught = new Button("Suspend grass growth");
            beforeDraught.setOnAction(event -> Platform.runLater(() -> {
                world.setDraught();
                activated = true;
                this.render();
            }));
        } else {
            beforeDraught = new Button("Disable");
            beforeDraught.setOnAction(event -> Platform.runLater(() -> {
                world.disableDraught();
                activated = false;
                this.render();
            }));

        }
        draughtController.getChildren().add(beforeDraught);

        return new HBox(draughtController);
    }


}

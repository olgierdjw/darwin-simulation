package org.world;

import javafx.application.Platform;
import javafx.scene.layout.HBox;

public abstract class GuiElement {
    HBox elementContainer;
    boolean firstRender = true;

    public GuiElement(HBox containerToBeUpdated) {
        this.elementContainer = containerToBeUpdated;
    }


    protected abstract HBox newInstance();

    protected boolean ignoreRenderRequest() {
        return false;
    }

    public void renderIfChanged() {
        if (ignoreRenderRequest()) {
            if (firstRender) firstRender = false;
            else return;
        }
        render();
    }

    public void render() {


        HBox updatedGui = newInstance();
        Platform.runLater(() -> {
            elementContainer.getChildren().clear();
            elementContainer.getChildren().add(updatedGui);
        });
    }

}

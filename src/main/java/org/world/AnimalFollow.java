package org.world;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.animal.Animal;

import java.util.Optional;

public class AnimalFollow extends GuiElement {

    World world;

    Optional<Animal> followedAnimal;

    public AnimalFollow(HBox containerToBeUpdated, World world) {
        super(containerToBeUpdated);
        this.world = world;
        this.followedAnimal = Optional.empty();
    }

    protected boolean ignoreRenderRequest() {
        return followedAnimal.isEmpty();
    }

    @Override
    public HBox newInstance() {
        VBox animalFollowPanel = new VBox(new Label("Follow")

        );
        if (followedAnimal.isPresent() && followedAnimal.get().getEnergy() > 0) {
            Animal getDetails = followedAnimal.get();
            animalFollowPanel.getChildren().add(new Label("Position: " + getDetails.getPosition().toString()));
            animalFollowPanel.getChildren().add(new Label("Energy: " + getDetails.getEnergy()));
            animalFollowPanel.getChildren().add(new Label("Children: " + getDetails.getChildrenCounter()));

            Button stopFollowingButton = new Button("Stop following");
            stopFollowingButton.setOnAction(event -> Platform.runLater(this::stopFollowing));
            animalFollowPanel.getChildren().add(stopFollowingButton);
        } else {
            animalFollowPanel.getChildren().add(animalSelector());
            followedAnimal = Optional.empty();
        }

        return new HBox(animalFollowPanel);
    }

    private HBox animalSelector() {
        TextField xField = new TextField("x");
        TextField yField = new TextField("y");
        xField.setPrefWidth(50);
        yField.setPrefWidth(50);
        Button findAnimal = new Button("Find");
        findAnimal.setOnAction(event -> {
            Platform.runLater(() -> {
                this.searchButtonHandler(xField.getText(), yField.getText());
            });

        });
        return new HBox(xField, yField, findAnimal);
    }

    private void searchButtonHandler(String argX, String argY) {
        int x, y;
        try {
            x = Integer.parseInt(argX);
            y = Integer.parseInt(argY);
        } catch (NumberFormatException e) {
            x = 0;
            y = 0;
        }

        this.followedAnimal = world.liveAnimal(new Vector2d(x, y));

        Platform.runLater(this::render);
    }

    public void stopFollowing() {
        this.followedAnimal = Optional.empty();
        this.render();
    }

    public Optional<Vector2d> positionToHighlight() {
        return followedAnimal.map(Animal::getPosition);
    }
}

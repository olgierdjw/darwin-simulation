package org.gui;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import org.animal.Animal;
import org.world.Grass;
import org.world.WorldSize;

import java.util.HashMap;

import static java.lang.Math.abs;

public class WorldGuiFactory {

    HashMap<Class<?>, Image> objectGraphics = new HashMap<>();

    public WorldGuiFactory() {
        objectGraphics.put(Grass.class, new Image("energy.png"));
    }

    public GridPane emptyWorldMap(WorldSize worldSize) {
        GridPane gridpane = new GridPane();
        gridpane.getStyleClass().add("grid");

        for (int i = 0; i < abs(worldSize.maxX() + 1); i++) {
            ColumnConstraints column = new ColumnConstraints(45);
            gridpane.getColumnConstraints().add(column);
        }
        for (int i = 0; i < abs(worldSize.maxY() + 1); i++) {
            RowConstraints row = new RowConstraints(45);
            gridpane.getRowConstraints().add(row);
        }
        gridpane.setGridLinesVisible(true);
        return gridpane;
    }


    public HBox gridElement(Grass grass) {
        HBox guiElement = new HBox();
        guiElement.setAlignment(Pos.CENTER);
        if (grass.energyValue() > 3)
            guiElement.getStyleClass().add("grass-boost");
        else
            guiElement.getStyleClass().add("grass");
        guiElement.getChildren().add(grassImageView());
        return guiElement;
    }


    public HBox gridElement(Animal animal, boolean highlight) {
        HBox guiElement = new HBox();
        if (highlight)
            guiElement.getStyleClass().add("highlight");
        else
            guiElement.getStyleClass().add("just-animal");
        guiElement.setAlignment(Pos.CENTER);
//        guiElement.getChildren().addAll(new Label(objectGraphics.get(animal.getClass())));
        guiElement.getChildren().add(new Label(animal.getPosition().toString()));
        return guiElement;
    }

    public HBox gridElement(Animal animal) {
        return gridElement(animal, false);
    }


    public HBox gridTextElement(String text) {
        HBox guiElement = new HBox();
        guiElement.setAlignment(Pos.CENTER);
        guiElement.getChildren().addAll(new Label(text));
        return guiElement;
    }


    private ImageView grassImageView() {
        Image image = objectGraphics.get(Grass.class);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(20);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setCache(true);
        return imageView;

    }
}

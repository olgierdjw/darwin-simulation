package org.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.settings.WorldSettings;
import org.simulation.GuiElements;
import org.simulation.Simulation;
import org.world.*;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MenuGui extends Application {

    ExecutorService simulationExecutor = Executors.newFixedThreadPool(5);
    LinkedHashMap<Simulation, Map<GuiElements, HBox>> simulationGuiReference = new LinkedHashMap<>();


    @Override
    public void start(Stage primaryStage) {

        Stage optionStage = new Stage();
        optionStage.setScene(new Scene(renderNewGameSettingsView()));
        optionStage.show();
        optionStage.setOnCloseRequest(e -> {
            Platform.exit();
            simulationGuiReference.forEach((simulation, settings) -> simulation.requestThreadEndOfLife());
            simulationExecutor.shutdownNow();
        });
        Platform.setImplicitExit(true);
    }

    private VBox renderNewGameSettingsView() {
        VBox menuWindow = new VBox();
        menuWindow.getStylesheets().add("menu-window.css");
        menuWindow.getStyleClass().add("menu-container");

        //default settings
        Button defaultOptionsStart = new Button("Start!");
        defaultOptionsStart.setOnAction(click -> newSimulation(new WorldSettings()));
        addGuiSection(menuWindow, "Use default settings", null, defaultOptionsStart);

//        //select your options
//        Button createInstanceByCurrentState = new Button("start using this settings");
////        createInstanceByCurrentState.setOnAction(click -> {
////            createNewSimulation(mapGuiSettingsToObject());
////        });
//        addGuiSection(menuWindow, "Use settings from below", null, createInstanceByCurrentState);
//
//        //own file settings
//        HBox content = new HBox();
//        TextField fileLocation = new TextField("lokalizacja");
//        Button createInstanceByOwnSettings = new Button("start using your own settings");
//        createInstanceByOwnSettings.setOnAction(click -> {
////            File ownSettings = new File(fileLocation + ".txt");
////            createNewSimulation(new Settings(ownSettings));
//        });
//        content.getChildren().addAll(fileLocation);
//        addGuiSection(menuWindow, "Use your own settings", content, createInstanceByOwnSettings);

        return menuWindow;
    }

    private void newSimulation(WorldSettings settings) {
        HBox container = new HBox();
        container.getStylesheets().add("simulation-window.css");
        container.getStyleClass().add("simulation-window");

        // charts
        VBox leftPanel = new VBox();
        leftPanel.getStyleClass().add("left-container");

        HBox populationChartPanel = new HBox();
        populationChartPanel.getStyleClass().add("panel-place");
        HBox avgEnergyChart = new HBox();
        avgEnergyChart.getStyleClass().add("panel-place");
        leftPanel.getChildren().addAll(populationChartPanel, avgEnergyChart);

        // main game container
        HBox gameMapGrid = new HBox(new Text("Liczba wątków w puli została wykorzystana."));
        gameMapGrid.getStyleClass().add("grid-container");
        VBox justForAlignment = new VBox(gameMapGrid);

        // right container
        VBox rightPanel = new VBox();

        HBox startPause = new HBox();
        HBox statusPanel = new HBox();
        HBox animalFollow = new HBox();
        HBox draughtMode = new HBox();
        List<HBox> hBoxes = Arrays.asList(startPause, statusPanel, animalFollow, draughtMode);
        hBoxes.forEach(hBox -> {
            hBox.getStyleClass().add("panel-place");
            rightPanel.getChildren().add(hBox);
        });

        container.getChildren().addAll(leftPanel, justForAlignment, rightPanel);

        Map<GuiElements, HBox> guiElementsLocation = Map.of(
                GuiElements.WORLD_DETAILS, statusPanel,
                GuiElements.WORLD_MAP, gameMapGrid,
                GuiElements.ANIMAL_DETAILS, animalFollow,
                GuiElements.START_PAUSE, startPause,
                GuiElements.POPULATION_CHART, populationChartPanel,
                GuiElements.ENERGY_CHART, avgEnergyChart,
                GuiElements.NATURAL_DISASTERS, draughtMode
        );

        // execute new thread
        Simulation newSimulation = new Simulation(this, container, settings, guiElementsLocation);
        simulationExecutor.submit(newSimulation);

        // don't lose this windows reference
        simulationGuiReference.put(newSimulation, guiElementsLocation);

        // stage (our new window)
        Stage oneStage = new Stage();
        oneStage.setOnCloseRequest(event -> {
            System.out.println("Closing simulation GUI");
            newSimulation.requestThreadEndOfLife();
            simulationGuiReference.remove(newSimulation);
        });

        int size = 890;
        Scene window = new Scene(container, size * 1.61, size);

        oneStage.setScene(window);
        oneStage.show();
    }

    ExecutorService wakeUpExecutor = Executors.newSingleThreadExecutor();

    public synchronized void wakeUpSimulation(Simulation simulationToWake) {
        wakeUpExecutor.submit(simulationToWake::resumeSimulation);
    }

    private void addGuiSection(VBox where, String optionName, HBox content, Button readyButton) {
        HBox menuScenario = new HBox();
        menuScenario.getStyleClass().add("container");
        menuScenario.getChildren().add(new Label(optionName));
        if (content != null) menuScenario.getChildren().add(content);
        menuScenario.getChildren().add(readyButton);
        where.getChildren().add(menuScenario);
    }
}

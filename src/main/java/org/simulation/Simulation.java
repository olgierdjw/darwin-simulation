package org.simulation;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.animal.Animal;
import org.gui.MenuGui;
import org.settings.WorldSettings;
import org.world.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class Simulation implements Runnable {

    protected World world;
    int iterationNumber = 0;
    boolean endThread = false;
    boolean simulationPause = false;
    private final String simulationName = Thread.currentThread().getName();


    MenuGui mainThread;
    HBox mainWindow;

    WorldSettings worldSettings;

    StatusPanel statusPanelGui;
    WorldMap worldMapGui;
    AnimalFollow animalFollowGui;
    StartPause startPause;
    PopulationChart populationChart;
    EnergyPerAnimalChart energyPerAnimalChart;
    DroughtController droughtController;

    public Simulation(MenuGui mainThread, HBox mainWindow, WorldSettings simulationSettings, Map<GuiElements, HBox> guiLocation) {
        worldSettings = new WorldSettings();
        world = new World(simulationSettings);


        this.mainThread = mainThread;
        this.mainWindow = mainWindow;

        // GUI
        statusPanelGui = new StatusPanel(guiLocation.get(GuiElements.WORLD_DETAILS), this, world);
        worldMapGui = new WorldMap(guiLocation.get(GuiElements.WORLD_MAP), this.world);
        animalFollowGui = new AnimalFollow(guiLocation.get(GuiElements.ANIMAL_DETAILS), this.world);
        world.setAnimalFollow(animalFollowGui);
        startPause = new StartPause(guiLocation.get(GuiElements.START_PAUSE), this);
        populationChart = new PopulationChart(guiLocation.get(GuiElements.POPULATION_CHART));
        energyPerAnimalChart = new EnergyPerAnimalChart(guiLocation.get(GuiElements.ENERGY_CHART));
        droughtController = new DroughtController(guiLocation.get(GuiElements.NATURAL_DISASTERS), world);

        world.initAnimals();
        System.out.print("Simulation init done.");

    }

    @Override
    public void run() {
        System.out.println("New Simulation, " + simulationName + " started.");

        startPause.render();
        worldMapGui.render();
        animalFollowGui.render();
        populationChart.render();
        energyPerAnimalChart.render();
        droughtController.render();
        while (!endThread) {
            waitIfNeeded(simulationPause);
            iterationNumber += 1;
            simulationDailyRoutine();
            animalFollowGui.renderIfChanged();
            worldMapGui.render();
            statusPanelGui.render();
            int animalCount = world.animalCount();
            populationChart.addRecord(iterationNumber, animalCount, world.energySum() / animalCount);
            energyPerAnimalChart.addRecord(iterationNumber, world.grassCount());
            waitInMilliseconds(200);
        }
        System.out.println("Simulation, " + simulationName + " ended.");
    }

    public int getIterationNumber() {
        return iterationNumber;
    }

    public synchronized void resumeSimulation() {
        System.out.println("main thread: wake up simulation thread!");
        notify();
    }

    public synchronized void waitIfNeeded(Boolean shouldPause) {
        if (!shouldPause) return;
        try {
            System.out.println("simulation wait()");
            wait();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void requestThreadEndOfLife() {
        endThread = true;
    }


    private String generateNewStats() {
        return "Statystyki obecnej klatki.";
    }

    private void waitInMilliseconds(int ms) {
        try {
            TimeUnit.MILLISECONDS.sleep(ms);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void simulationDailyRoutine() {
        world.deleteDeadAnimals();
        world.animalMove();
        world.feedAnimals();
        world.procreation();
        world.grassGrow();
    }


    public HBox renderButtons() {
        Button controlStateButton;
        if (simulationPause) {
            controlStateButton = new Button("Continue");
            controlStateButton.setOnAction((event) -> {
                simulationPause = false;
                Platform.runLater(() -> {
                    mainWindow.setStyle("-fx-border-color: green");
                    startPause.render();
                    mainThread.wakeUpSimulation(this);
                });
            });
        } else {
            controlStateButton = new Button("Pause");
            controlStateButton.setOnAction((event) -> {
                simulationPause = true;
                Platform.runLater(() -> startPause.render());
                mainWindow.setStyle("-fx-border-color: red");
            });
        }
        VBox infoPanel = new VBox(controlStateButton);
        return new HBox(infoPanel);
    }
}

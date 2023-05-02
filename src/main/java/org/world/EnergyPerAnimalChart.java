package org.world;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.HBox;

public class EnergyPerAnimalChart extends GuiElement {

    private final NumberAxis xAxis = new NumberAxis();
    private final NumberAxis yAxis = new NumberAxis();
    private final LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
    private final XYChart.Series<Number, Number> series1;

    public EnergyPerAnimalChart(HBox containerToBeUpdated) {
        super(containerToBeUpdated);
        xAxis.setLabel("Day");
        chart.setCreateSymbols(false);
        series1 = new XYChart.Series<>();
        series1.setName("Average animal energy");
        chart.getData().addAll(series1);

    }

    public HBox createGraph() {
        HBox hbox = new HBox(chart);
        hbox.setPadding(new Insets(10));
        hbox.setSpacing(1000);
        return hbox;
    }

    @Override
    protected HBox newInstance() {
        return createGraph();
    }

    public void addRecord(int iterationDay, int energyPerCaputa) {
        Platform.runLater(() -> {
            series1.getData().add(new XYChart.Data<>(iterationDay, energyPerCaputa));
        });
    }
}

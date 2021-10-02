package io.github.fireres.gui.controller;

import javafx.scene.chart.LineChart;
import javafx.scene.layout.StackPane;

public interface ChartContainer {

    LineChart getChart();

    StackPane getStackPane();

    void synchronizeChart();

}

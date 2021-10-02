package io.github.fireres.gui.controller;

import javafx.scene.Node;

import java.util.List;
import java.util.UUID;

public interface ReportUpdater {

    void updateReport(Runnable action, Node... nodesToLock);

    void updateReport(Runnable action, List<Node> nodesToLock);

    ChartContainer getChartContainer();

    UUID getUpdatingElementId();

}

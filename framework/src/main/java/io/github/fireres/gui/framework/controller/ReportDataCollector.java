package io.github.fireres.gui.framework.controller;

import javafx.scene.control.TableView;

import java.util.Map;

public interface ReportDataCollector {

    TableView<Map<String, Number>> getReportData();

}

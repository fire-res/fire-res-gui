package io.github.fireres.gui.controller;

import io.github.fireres.core.model.Report;
import io.github.fireres.core.properties.ReportProperties;

public interface ReportContainer<P extends ReportProperties, R extends Report<P>> extends SampleContainer {

    R getReport();

    ChartContainer getChartContainer();

}

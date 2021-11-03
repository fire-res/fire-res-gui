package io.github.fireres.gui.controller;

import io.github.fireres.core.properties.ReportProperties;

public interface ReportCreator<P extends ReportProperties> {

    void createReport(P properties);

}

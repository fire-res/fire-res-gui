package io.github.fireres.gui.framework.controller;

import io.github.fireres.core.model.ReportType;
import io.github.fireres.core.properties.GeneralProperties;
import javafx.scene.control.Tab;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class ReportTab
        extends AbstractReportUpdaterComponent<Tab>
        implements
        ReportInclusionChanger, PresetChanger, Refreshable {

    public abstract ReportType getReportType();

    @Autowired
    private GeneralProperties generalProperties;

    @Override
    public void postConstruct() {
        getComponent().setText(getReportType().getDescription());
        excludeReportIfNeeded();
    }

    private void excludeReportIfNeeded() {
        if (isReportExcluded()) {
            excludeReport();
        }
    }

    public boolean isReportExcluded() {
        return !generalProperties.getIncludedReports().contains(getReportType());
    }

}

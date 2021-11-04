package io.github.fireres.gui.unheated.surface.controller.groups.primary;

import io.github.fireres.core.model.Sample;
import io.github.fireres.gui.framework.annotation.FxmlView;
import io.github.fireres.gui.framework.controller.AbstractComponent;
import io.github.fireres.gui.framework.controller.ChartContainer;
import io.github.fireres.gui.unheated.surface.controller.UnheatedSurfaceReportContainer;
import io.github.fireres.gui.unheated.surface.synchronizer.PrimaryGroupChartSynchronizer;
import io.github.fireres.unheated.surface.report.UnheatedSurfaceReport;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.layout.StackPane;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@FxmlView("primaryGroupChart.fxml")
@RequiredArgsConstructor
@Component
@Scope(scopeName = SCOPE_PROTOTYPE)
public class PrimaryGroupChart extends AbstractComponent<StackPane>
        implements UnheatedSurfaceReportContainer, ChartContainer {

    @FXML
    private LineChart<Number, Number> chart;

    private final PrimaryGroupChartSynchronizer chartSynchronizer;

    @Override
    public UnheatedSurfaceReport getReport() {
        return ((PrimaryGroup) getParent()).getReport();
    }

    @Override
    public ChartContainer getChartContainer() {
        return this;
    }

    @Override
    public Sample getSample() {
        return ((PrimaryGroup) getParent()).getSample();
    }

    @Override
    public LineChart getChart() {
        return chart;
    }

    @Override
    public StackPane getStackPane() {
        return getComponent();
    }

    @Override
    public void synchronizeChart() {
        chartSynchronizer.synchronize(chart, getReport());
    }
}

package io.github.fireres.gui.controller.excess.pressure;

import io.github.fireres.core.model.Sample;
import io.github.fireres.excess.pressure.report.ExcessPressureReport;
import io.github.fireres.gui.controller.AbstractComponent;
import io.github.fireres.gui.controller.ChartContainer;
import io.github.fireres.gui.service.ChartsSynchronizationService;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.layout.StackPane;
import lombok.RequiredArgsConstructor;
import io.github.fireres.gui.annotation.FxmlView;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@FxmlView("excessPressureChart.fxml")
@RequiredArgsConstructor
@Component
@Scope(scopeName = SCOPE_PROTOTYPE)
public class ExcessPressureChart extends AbstractComponent<StackPane>
        implements ExcessPressureReportContainer, ChartContainer {

    @FXML
    private LineChart<Number, Number> chart;

    private final ChartsSynchronizationService chartsSynchronizationService;

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
        chartsSynchronizationService.syncExcessPressureChart(chart, getReport());
    }

    @Override
    public ExcessPressureReport getReport() {
        return ((ExcessPressure) getParent()).getReport();
    }

    @Override
    public ChartContainer getChartContainer() {
        return this;
    }

    @Override
    public Sample getSample() {
        return ((ExcessPressure) getParent()).getSample();
    }
}

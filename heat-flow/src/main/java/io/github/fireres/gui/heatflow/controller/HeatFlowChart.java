package io.github.fireres.gui.heatflow.controller;

import io.github.fireres.core.model.Sample;
import io.github.fireres.gui.framework.annotation.FxmlView;
import io.github.fireres.gui.framework.controller.AbstractComponent;
import io.github.fireres.gui.framework.controller.ChartContainer;
import io.github.fireres.gui.heatflow.synchronizer.HeatFlowChartSynchronizer;
import io.github.fireres.heatflow.report.HeatFlowReport;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.layout.StackPane;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@FxmlView("heatFlowChart.fxml")
@RequiredArgsConstructor
@Component
@Scope(scopeName = SCOPE_PROTOTYPE)
public class HeatFlowChart extends AbstractComponent<StackPane>
        implements HeatFlowReportContainer, ChartContainer {

    @FXML
    private LineChart<Number, Number> chart;

    private final HeatFlowChartSynchronizer chartSynchronizer;

    @Override
    public HeatFlowReport getReport() {
        return ((HeatFlow) getParent()).getReport();
    }

    @Override
    public ChartContainer getChartContainer() {
        return this;
    }

    @Override
    public Sample getSample() {
        return ((HeatFlow) getParent()).getSample();
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

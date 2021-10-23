package io.github.fireres.gui.controller.heat.flow;

import io.github.fireres.core.model.Sample;
import io.github.fireres.gui.controller.AbstractReportUpdaterComponent;
import io.github.fireres.gui.controller.ChartContainer;
import io.github.fireres.gui.controller.ReportUpdater;
import io.github.fireres.heatflow.report.HeatFlowReport;
import io.github.fireres.heatflow.service.HeatFlowService;
import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import javafx.scene.control.TitledPane;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import io.github.fireres.gui.annotation.FxmlView;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@FxmlView("heatFlowParams.fxml")
@RequiredArgsConstructor
@Component
@Scope(scopeName = SCOPE_PROTOTYPE)
public class HeatFlowParams extends AbstractReportUpdaterComponent<TitledPane>
        implements HeatFlowReportContainer {

    @FXML
    @Getter
    private Spinner<Integer> sensors;

    @FXML
    @Getter
    private Spinner<Double> bound;

    private final HeatFlowService heatFlowService;

    @FXML
    public void handleSensorCountChanged() {
        updateReport(
                () -> heatFlowService.updateSensorsCount(getReport(), sensors.getValue()),
                ((HeatFlow) getParent()).getParamsVbox());
    }

    @FXML
    public void handleHeatFlowBoundChanged() {
        updateReport(
                () -> heatFlowService.updateBound(getReport(), bound.getValue()),
                ((HeatFlow) getParent()).getParamsVbox());
    }

    @Override
    public Sample getSample() {
        return ((HeatFlow) getParent()).getSample();
    }

    @Override
    public HeatFlowReport getReport() {
        return ((HeatFlow) getParent()).getReport();
    }

    @Override
    public ChartContainer getChartContainer() {
        return ((HeatFlow) getParent()).getChartContainer();
    }

    @Override
    public UUID getUpdatingElementId() {
        return ((ReportUpdater) getParent()).getUpdatingElementId();
    }
}

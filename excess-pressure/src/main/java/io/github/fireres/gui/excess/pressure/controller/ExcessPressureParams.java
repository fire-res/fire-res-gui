package io.github.fireres.gui.excess.pressure.controller;

import io.github.fireres.core.model.Sample;
import io.github.fireres.excess.pressure.report.ExcessPressureReport;
import io.github.fireres.excess.pressure.service.ExcessPressureService;
import io.github.fireres.gui.framework.annotation.FxmlView;
import io.github.fireres.gui.framework.controller.AbstractReportUpdaterComponent;
import io.github.fireres.gui.framework.controller.ChartContainer;
import io.github.fireres.gui.framework.controller.ReportUpdater;
import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import javafx.scene.control.TitledPane;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@FxmlView("excessPressureParams.fxml")
@RequiredArgsConstructor
@Component
@Scope(scopeName = SCOPE_PROTOTYPE)
public class ExcessPressureParams extends AbstractReportUpdaterComponent<TitledPane>
        implements ExcessPressureReportContainer {

    @FXML
    @Getter
    private Spinner<Double> basePressure;

    @FXML
    @Getter
    private Spinner<Double> dispersionCoefficient;

    @FXML
    @Getter
    private Spinner<Double> delta;

    private final ExcessPressureService excessPressureService;

    @FXML
    public void handleBasePressureChanged() {
        updateReport(
                () -> excessPressureService.updateBasePressure(getReport(), basePressure.getValue()),
                ((ExcessPressure) getParent()).getParamsVbox());
    }

    @FXML
    public void handleDispersionCoefficientChanged() {
        updateReport(
                () -> excessPressureService.updateDispersionCoefficient(getReport(), dispersionCoefficient.getValue()),
                ((ExcessPressure) getParent()).getParamsVbox());
    }

    @FXML
    public void handleDeltaSpinnerChanged() {
        updateReport(
                () -> excessPressureService.updateDelta(getReport(), delta.getValue()),
                ((ExcessPressure) getParent()).getParamsVbox());
    }

    @Override
    public Sample getSample() {
        return ((ExcessPressure) getParent()).getSample();
    }

    @Override
    public ExcessPressureReport getReport() {
        return ((ExcessPressure) getParent()).getReport();
    }

    @Override
    public ChartContainer getChartContainer() {
        return ((ExcessPressure) getParent()).getChartContainer();
    }

    @Override
    public UUID getReportId() {
        return ((ReportUpdater) getParent()).getReportId();
    }
}

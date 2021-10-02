package io.github.fireres.gui.controller.excess.pressure;

import io.github.fireres.core.model.Sample;
import io.github.fireres.excess.pressure.report.ExcessPressureReport;
import io.github.fireres.excess.pressure.service.ExcessPressureService;
import io.github.fireres.gui.controller.AbstractReportUpdaterComponent;
import io.github.fireres.gui.controller.ChartContainer;
import io.github.fireres.gui.controller.ReportUpdater;
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

    @Override
    protected void initialize() {
        basePressure.focusedProperty().addListener((observable, oldValue, newValue) ->
                handleBasePressureSpinnerLostFocus(newValue));

        dispersionCoefficient.focusedProperty().addListener((observable, oldValue, newValue) ->
                handleDispersionCoefficientLostFocus(newValue));

        delta.focusedProperty().addListener((observable, oldValue, newValue) ->
                handleDeltaSpinnerLostFocus(newValue));
    }

    private void handleBasePressureSpinnerLostFocus(Boolean focusValue) {
        Runnable action = () ->
                excessPressureService.updateBasePressure(getReport(), basePressure.getValue());

        handleSpinnerLostFocus(focusValue, basePressure, () ->
                updateReport(action, ((ExcessPressure) getParent()).getParamsVbox()));
    }

    private void handleDispersionCoefficientLostFocus(Boolean focusValue) {
        Runnable action = () ->
                excessPressureService.updateDispersionCoefficient(getReport(), dispersionCoefficient.getValue());

        handleSpinnerLostFocus(focusValue, dispersionCoefficient, () ->
                updateReport(action, ((ExcessPressure) getParent()).getParamsVbox()));
    }

    private void handleDeltaSpinnerLostFocus(Boolean focusValue) {
        Runnable action = () ->
                excessPressureService.updateDelta(getReport(), delta.getValue());

        handleSpinnerLostFocus(focusValue, delta, () ->
                updateReport(action, ((ExcessPressure) getParent()).getParamsVbox()));
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
    public UUID getUpdatingElementId() {
        return ((ReportUpdater) getParent()).getUpdatingElementId();
    }
}

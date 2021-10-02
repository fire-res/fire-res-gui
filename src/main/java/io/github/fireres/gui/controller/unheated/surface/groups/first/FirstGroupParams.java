package io.github.fireres.gui.controller.unheated.surface.groups.first;

import io.github.fireres.core.model.Sample;
import io.github.fireres.gui.controller.AbstractReportUpdaterComponent;
import io.github.fireres.gui.controller.ChartContainer;
import io.github.fireres.gui.controller.ReportUpdater;
import io.github.fireres.gui.controller.unheated.surface.UnheatedSurfaceReportContainer;
import io.github.fireres.unheated.surface.report.UnheatedSurfaceReport;
import io.github.fireres.unheated.surface.service.UnheatedSurfaceFirstGroupService;
import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import javafx.scene.control.TitledPane;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import io.github.fireres.gui.annotation.FxmlView;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@FxmlView("firstGroupParams.fxml")
@RequiredArgsConstructor
@Component
@Scope(scopeName = SCOPE_PROTOTYPE)
public class FirstGroupParams extends AbstractReportUpdaterComponent<TitledPane>
        implements UnheatedSurfaceReportContainer {

    @FXML
    @Getter
    private Spinner<Integer> thermocouples;

    private final UnheatedSurfaceFirstGroupService unheatedSurfaceFirstGroupService;

    @SneakyThrows
    private void handleThermocouplesCountSpinnerFocusChanged(Boolean focusValue) {
        Runnable action = () ->
                unheatedSurfaceFirstGroupService.updateThermocoupleCount(
                        getReport(),
                        thermocouples.getValue());

        handleSpinnerLostFocus(focusValue, thermocouples, () ->
                updateReport(action, ((FirstGroup) getParent()).getParamsVbox()));
    }

    @Override
    protected void initialize() {
        thermocouples.focusedProperty().addListener((observable, oldValue, newValue) ->
                handleThermocouplesCountSpinnerFocusChanged(newValue));
    }

    @Override
    public Sample getSample() {
        return ((FirstGroup) getParent()).getSample();
    }

    @Override
    public UnheatedSurfaceReport getReport() {
        return ((FirstGroup) getParent()).getReport();
    }

    @Override
    public ChartContainer getChartContainer() {
        return ((FirstGroup) getParent()).getChartContainer();
    }

    @Override
    public UUID getUpdatingElementId() {
        return ((ReportUpdater) getParent()).getUpdatingElementId();
    }

}

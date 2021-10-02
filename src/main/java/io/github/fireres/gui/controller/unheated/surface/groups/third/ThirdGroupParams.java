package io.github.fireres.gui.controller.unheated.surface.groups.third;

import io.github.fireres.core.model.Sample;
import io.github.fireres.gui.controller.AbstractReportUpdaterComponent;
import io.github.fireres.gui.controller.ChartContainer;
import io.github.fireres.gui.controller.ReportUpdater;
import io.github.fireres.gui.controller.unheated.surface.UnheatedSurfaceReportContainer;
import io.github.fireres.unheated.surface.report.UnheatedSurfaceReport;
import io.github.fireres.unheated.surface.service.UnheatedSurfaceThirdGroupService;
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

@FxmlView("thirdGroupParams.fxml")
@RequiredArgsConstructor
@Component
@Scope(scopeName = SCOPE_PROTOTYPE)
public class ThirdGroupParams extends AbstractReportUpdaterComponent<TitledPane>
        implements UnheatedSurfaceReportContainer {

    @FXML
    @Getter
    private Spinner<Integer> thermocouples;

    @FXML
    @Getter
    private Spinner<Integer> bound;

    private final UnheatedSurfaceThirdGroupService unheatedSurfaceThirdGroupService;

    @Override
    protected void initialize() {
        thermocouples.focusedProperty().addListener((observable, oldValue, newValue) ->
                handleThermocouplesCountSpinnerFocusChanged(newValue));

        bound.focusedProperty().addListener((observable, oldValue, newValue) ->
                handleThermocouplesBoundSpinnerFocusChanged(newValue));
    }

    private void handleThermocouplesCountSpinnerFocusChanged(Boolean focusValue) {
        Runnable action = () ->
                unheatedSurfaceThirdGroupService.updateThermocoupleCount(
                        getReport(),
                        thermocouples.getValue());

        handleSpinnerLostFocus(focusValue, thermocouples, () ->
                updateReport(action, ((ThirdGroup) getParent()).getParamsVbox()));
    }

    private void handleThermocouplesBoundSpinnerFocusChanged(Boolean focusValue) {
        Runnable action = () ->
                unheatedSurfaceThirdGroupService.updateBound(
                        getReport(),
                        bound.getValue());

        handleSpinnerLostFocus(focusValue, bound, () ->
                updateReport(action, ((ThirdGroup) getParent()).getParamsVbox()));
    }

    @Override
    public Sample getSample() {
        return ((ThirdGroup) getParent()).getSample();
    }

    @Override
    public UnheatedSurfaceReport getReport() {
        return ((ThirdGroup) getParent()).getReport();
    }

    @Override
    public ChartContainer getChartContainer() {
        return ((ThirdGroup) getParent()).getChartContainer();
    }

    @Override
    public UUID getUpdatingElementId() {
        return ((ReportUpdater) getParent()).getUpdatingElementId();
    }

}

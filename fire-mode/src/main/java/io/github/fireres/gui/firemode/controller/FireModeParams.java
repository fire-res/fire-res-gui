package io.github.fireres.gui.firemode.controller;

import io.github.fireres.core.model.Sample;
import io.github.fireres.firemode.model.FireModeType;
import io.github.fireres.firemode.report.FireModeReport;
import io.github.fireres.firemode.service.FireModeService;
import io.github.fireres.gui.framework.annotation.FxmlView;
import io.github.fireres.gui.framework.component.FireResChoiceBox;
import io.github.fireres.gui.framework.controller.AbstractReportUpdaterComponent;
import io.github.fireres.gui.framework.controller.ChartContainer;
import io.github.fireres.gui.framework.controller.ReportUpdater;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.TitledPane;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@FxmlView("fireModeParams.fxml")
@RequiredArgsConstructor
@Component
@Scope(scopeName = SCOPE_PROTOTYPE)
public class FireModeParams extends AbstractReportUpdaterComponent<TitledPane>
        implements FireModeReportContainer {

    @FXML
    @Getter
    private CheckBox showMeanTemperature;

    @FXML
    @Getter
    private CheckBox showBounds;

    @FXML
    @Getter
    private Spinner<Integer> temperatureMaintaining;

    @FXML
    @Getter
    private FireResChoiceBox<FireModeType> fireModeType;

    @FXML
    @Getter
    private Spinner<Integer> thermocouples;

    private final FireModeService fireModeService;

    @Override
    public void postConstruct() {
        fireModeType.getItems().addAll(FireModeType.values());
    }

    @FXML
    public void handleThermocoupleCountChanged() {
        updateReport(
                () -> fireModeService.updateThermocoupleCount(getReport(), thermocouples.getValue()),
                ((FireMode) getParent()).getParamsVbox());
    }

    @FXML
    public void handleTemperatureMaintainingChanged() {
        updateReport(() -> {
            val value = temperatureMaintaining.getValue();

            fireModeService.updateTemperatureMaintaining(getReport(), value == 0 ? null : value);
        }, ((FireMode) getParent()).getParamsVbox());
    }

    @FXML
    public void handleFireModeTypeChanged() {
        updateReport(
                () -> fireModeService.updateFireModeType(getReport(), fireModeType.getValue()),
                ((FireMode) getParent()).getParamsVbox());
    }

    @FXML
    public void handleShowBoundsChanged() {
        updateReport(
                () -> fireModeService.updateShowBounds(getReport(), showBounds.isSelected()),
                ((FireMode) getParent()).getParamsVbox());
    }

    @FXML
    public void handleShowMeanTemperatureChanged() {
        updateReport(
                () -> fireModeService.updateShowMeanTemperature(getReport(), showMeanTemperature.isSelected()),
                ((FireMode) getParent()).getParamsVbox());
    }

    @Override
    public Sample getSample() {
        return ((FireMode) getParent()).getSample();
    }

    @Override
    public FireModeReport getReport() {
        return ((FireMode) getParent()).getReport();
    }

    @Override
    public ChartContainer getChartContainer() {
        return ((FireMode) getParent()).getChartContainer();
    }

    @Override
    public UUID getReportId() {
        return ((ReportUpdater) getParent()).getReportId();
    }
}

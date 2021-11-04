package io.github.fireres.gui.firemode.controller;

import io.github.fireres.core.model.ReportType;
import io.github.fireres.core.model.Sample;
import io.github.fireres.core.properties.GeneralProperties;
import io.github.fireres.firemode.properties.FireModeProperties;
import io.github.fireres.firemode.report.FireModeReport;
import io.github.fireres.firemode.service.FireModeService;
import io.github.fireres.gui.firemode.initializer.FireModeInitializer;
import io.github.fireres.gui.firemode.preset.FireModePresetApplier;
import io.github.fireres.gui.framework.annotation.FxmlView;
import io.github.fireres.gui.framework.annotation.Initialize;
import io.github.fireres.gui.framework.controller.ChartContainer;
import io.github.fireres.gui.framework.controller.PresetContainer;
import io.github.fireres.gui.framework.controller.ReportCreator;
import io.github.fireres.gui.framework.controller.ReportDataCollector;
import io.github.fireres.gui.framework.controller.ReportTab;
import io.github.fireres.gui.framework.controller.Resettable;
import io.github.fireres.gui.framework.controller.common.BoundsShiftParams;
import io.github.fireres.gui.framework.controller.common.FunctionParams;
import io.github.fireres.gui.framework.controller.common.ReportToolBar;
import io.github.fireres.gui.framework.controller.common.SampleTab;
import io.github.fireres.gui.framework.preset.Preset;
import io.github.fireres.gui.framework.service.DataViewService;
import io.github.fireres.gui.framework.service.TabService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.val;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

import static io.github.fireres.firemode.report.FireModeReportType.FIRE_MODE;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@FxmlView("fireMode.fxml")
@RequiredArgsConstructor
@Component
@Scope(scopeName = SCOPE_PROTOTYPE)
@Initialize(FireModeInitializer.class)
public class FireMode extends ReportTab
        implements
        FireModeReportContainer,
        Resettable,
        ReportDataCollector,
        ReportCreator<FireModeProperties> {

    @FXML
    @Getter
    private VBox paramsVbox;

    @Getter
    @Setter
    private FireModeReport report;

    @FXML
    private FireModeParams fireModeParamsController;

    @FXML
    private FunctionParams functionParamsController;

    @FXML
    private FireModeChart fireModeChartController;

    @FXML
    private BoundsShiftParams boundsShiftParamsController;

    @FXML
    private ReportToolBar toolBarController;

    private final FireModeService fireModeService;
    private final GeneralProperties generalProperties;
    private final DataViewService dataViewService;
    private final FireModePresetApplier presetApplier;
    private final TabService tabService;

    @Override
    public void refresh() {
        if (!isReportExcluded()) {
            createReport(report.getProperties());
        }
    }

    @Override
    public void reset() {
        updateReport(
                () -> changePreset(((PresetContainer) getParent()).getPreset()),
                getParamsVbox());
    }

    @Override
    public void changePreset(Preset preset) {
        Platform.runLater(() -> presetApplier.apply(this, preset));
    }

    @Override
    public void createReport(FireModeProperties properties) {
        updateReport(
                () -> {
                    getSample().removeReport(report);
                    this.report = fireModeService.createReport(getSample(), properties);
                },
                getParamsVbox());
    }

    @Override
    public void excludeReport() {
        getSample().removeReport(report);
        tabService.disableTab(getComponent());
        generalProperties.getIncludedReports().removeIf(FIRE_MODE::equals);
    }

    @Override
    public void includeReport() {
        val parent = ((SampleTab) getParent());

        getSample().addReport(report);
        tabService.enableTab(getComponent(), parent.getReportsTabPane());
        generalProperties.getIncludedReports().add(FIRE_MODE);
    }

    @Override
    public TableView<Map<String, Number>> getReportData() {
        return dataViewService.getDataViewer(report);
    }

    @Override
    public Sample getSample() {
        return ((SampleTab) getParent()).getSample();
    }

    public FireModeParams getFireModeParams() {
        return fireModeParamsController;
    }

    public FunctionParams getFunctionParams() {
        return functionParamsController;
    }

    public BoundsShiftParams getBoundsShiftParams() {
        return boundsShiftParamsController;
    }

    public ReportToolBar getToolBar() {
        return toolBarController;
    }

    @Override
    public ChartContainer getChartContainer() {
        return fireModeChartController;
    }

    @Override
    public UUID getReportId() {
        return report == null ? null : report.getId();
    }

    @Override
    public ReportType getReportType() {
        return FIRE_MODE;
    }
}

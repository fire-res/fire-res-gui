package io.github.fireres.gui.excess.pressure.controller;

import io.github.fireres.core.model.ReportType;
import io.github.fireres.core.model.Sample;
import io.github.fireres.core.properties.GeneralProperties;
import io.github.fireres.excess.pressure.properties.ExcessPressureProperties;
import io.github.fireres.excess.pressure.report.ExcessPressureReport;
import io.github.fireres.excess.pressure.service.ExcessPressureService;
import io.github.fireres.gui.excess.pressure.initializer.ExcessPressureInitializer;
import io.github.fireres.gui.excess.pressure.preset.ExcessPressurePresetApplier;
import io.github.fireres.gui.framework.annotation.FxmlView;
import io.github.fireres.gui.framework.annotation.Initialize;
import io.github.fireres.gui.framework.controller.ChartContainer;
import io.github.fireres.gui.framework.controller.PresetContainer;
import io.github.fireres.gui.framework.controller.ReportCreator;
import io.github.fireres.gui.framework.controller.ReportDataCollector;
import io.github.fireres.gui.framework.controller.ReportTab;
import io.github.fireres.gui.framework.controller.Resettable;
import io.github.fireres.gui.framework.controller.common.BoundsShiftParams;
import io.github.fireres.gui.framework.controller.common.ReportToolBar;
import io.github.fireres.gui.framework.controller.common.SampleTab;
import io.github.fireres.gui.framework.preset.Preset;
import io.github.fireres.gui.framework.service.DataViewService;
import io.github.fireres.gui.framework.service.ReportTabService;
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

import static io.github.fireres.excess.pressure.report.ExcessPressureReportType.EXCESS_PRESSURE;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@FxmlView("excessPressure.fxml")
@RequiredArgsConstructor
@Component
@Scope(scopeName = SCOPE_PROTOTYPE)
@Initialize(ExcessPressureInitializer.class)
public class ExcessPressure extends ReportTab
        implements
        ExcessPressureReportContainer,
        Resettable,
        ReportDataCollector,
        ReportCreator<ExcessPressureProperties> {

    @FXML
    @Getter
    private VBox paramsVbox;

    @Getter
    @Setter
    private ExcessPressureReport report;

    @FXML
    private ExcessPressureParams excessPressureParamsController;

    @FXML
    private ExcessPressureChart excessPressureChartController;

    @FXML
    private BoundsShiftParams boundsShiftParamsController;

    @FXML
    private ReportToolBar toolBarController;

    private final ExcessPressureService excessPressureService;
    private final GeneralProperties generalProperties;
    private final DataViewService dataViewService;
    private final ExcessPressurePresetApplier presetApplier;
    private final ReportTabService tabService;

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
    public void createReport(ExcessPressureProperties properties) {
        updateReport(
                () -> {
                    getSample().removeReport(report);
                    this.report = excessPressureService.createReport(getSample(), properties);
                },
                getParamsVbox());
    }

    @Override
    public void excludeReport() {
        tabService.disableTab(getComponent());
        generalProperties.getIncludedReports().removeIf(EXCESS_PRESSURE::equals);
    }

    @Override
    public void includeReport() {
        val parent = (SampleTab) getParent();

        tabService.enableTab(getComponent(), parent.getReportsTabPane());
        generalProperties.getIncludedReports().add(EXCESS_PRESSURE);
    }

    @Override
    public TableView<Map<String, Number>> getReportData() {
        return dataViewService.getDataViewer(report);
    }

    @Override
    public Sample getSample() {
        return ((SampleTab) getParent()).getSample();
    }

    public BoundsShiftParams getBoundsShiftParams() {
        return boundsShiftParamsController;
    }

    public ReportToolBar getToolBar() {
        return toolBarController;
    }

    public ExcessPressureParams getExcessPressureParams() {
        return excessPressureParamsController;
    }

    @Override
    public ChartContainer getChartContainer() {
        return excessPressureChartController;
    }

    @Override
    public UUID getReportId() {
        return report == null ? null : report.getId();
    }

    @Override
    public ReportType getReportType() {
        return EXCESS_PRESSURE;
    }

}

package io.github.fireres.gui.heatflow.controller;

import io.github.fireres.core.model.ReportType;
import io.github.fireres.core.model.Sample;
import io.github.fireres.core.properties.GeneralProperties;
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
import io.github.fireres.gui.framework.service.ReportTabService;
import io.github.fireres.gui.heatflow.initializer.HeatFlowInitializer;
import io.github.fireres.gui.heatflow.preset.HeatFlowPresetApplier;
import io.github.fireres.heatflow.properties.HeatFlowProperties;
import io.github.fireres.heatflow.report.HeatFlowReport;
import io.github.fireres.heatflow.service.HeatFlowService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

import static io.github.fireres.heatflow.report.HeatFlowReportType.HEAT_FLOW;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@FxmlView("heatFlow.fxml")
@RequiredArgsConstructor
@Component
@Scope(scopeName = SCOPE_PROTOTYPE)
@Initialize(HeatFlowInitializer.class)
public class HeatFlow extends ReportTab
        implements
        HeatFlowReportContainer,
        Resettable,
        ReportDataCollector,
        ReportCreator<HeatFlowProperties> {

    @FXML
    @Getter
    private VBox paramsVbox;

    @Getter
    private HeatFlowReport report;

    @FXML
    private HeatFlowParams heatFlowParamsController;

    @FXML
    private HeatFlowChart heatFlowChartController;

    @FXML
    private FunctionParams functionParamsController;

    @FXML
    private BoundsShiftParams boundsShiftParamsController;

    @FXML
    private ReportToolBar toolBarController;

    private final HeatFlowService heatFlowService;
    private final GeneralProperties generalProperties;
    private final DataViewService dataViewService;
    private final HeatFlowPresetApplier presetApplier;
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
    public void createReport(HeatFlowProperties properties) {
        updateReport(
                () -> {
                    getSample().removeReport(report);
                    this.report = heatFlowService.createReport(getSample(), properties);
                },
                getParamsVbox());
    }

    @Override
    public void excludeReport() {
        tabService.disableTab(getComponent());
        generalProperties.getIncludedReports().removeIf(HEAT_FLOW::equals);
    }

    @Override
    public void includeReport() {
        val parent = ((SampleTab) getParent());

        tabService.enableTab(getComponent(), parent.getReportsTabPane());
        generalProperties.getIncludedReports().add(HEAT_FLOW);
    }

    @Override
    public TableView<Map<String, Number>> getReportData() {
        return dataViewService.getDataViewer(report);
    }

    @Override
    public Sample getSample() {
        return ((SampleTab) getParent()).getSample();
    }

    public HeatFlowParams getHeatFlowParams() {
        return heatFlowParamsController;
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
        return heatFlowChartController;
    }

    @Override
    public UUID getReportId() {
        return report == null ? null : report.getId();
    }

    @Override
    public ReportType getReportType() {
        return HEAT_FLOW;
    }
}

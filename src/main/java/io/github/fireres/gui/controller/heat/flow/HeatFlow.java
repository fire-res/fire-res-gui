package io.github.fireres.gui.controller.heat.flow;

import io.github.fireres.core.model.Sample;
import io.github.fireres.core.properties.GeneralProperties;
import io.github.fireres.excel.report.HeatFlowExcelReportsBuilder;
import io.github.fireres.gui.annotation.FxmlView;
import io.github.fireres.gui.annotation.Initialize;
import io.github.fireres.gui.component.DataViewer;
import io.github.fireres.gui.controller.AbstractReportUpdaterComponent;
import io.github.fireres.gui.controller.ChartContainer;
import io.github.fireres.gui.controller.PresetChanger;
import io.github.fireres.gui.controller.PresetContainer;
import io.github.fireres.gui.controller.Refreshable;
import io.github.fireres.gui.controller.ReportCreator;
import io.github.fireres.gui.controller.ReportDataCollector;
import io.github.fireres.gui.controller.ReportInclusionChanger;
import io.github.fireres.gui.controller.Resettable;
import io.github.fireres.gui.controller.common.BoundsShiftParams;
import io.github.fireres.gui.controller.common.FunctionParams;
import io.github.fireres.gui.controller.common.ReportToolBar;
import io.github.fireres.gui.controller.common.SampleTab;
import io.github.fireres.gui.initializer.report.HeatFlowInitializer;
import io.github.fireres.gui.preset.Preset;
import io.github.fireres.gui.preset.impl.HeatFlowPresetApplier;
import io.github.fireres.heatflow.properties.HeatFlowProperties;
import io.github.fireres.heatflow.report.HeatFlowReport;
import io.github.fireres.heatflow.service.HeatFlowService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static io.github.fireres.core.properties.ReportType.HEAT_FLOW;
import static io.github.fireres.gui.util.TabUtils.disableTab;
import static io.github.fireres.gui.util.TabUtils.enableTab;
import static java.util.Collections.singletonList;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@FxmlView("heatFlow.fxml")
@RequiredArgsConstructor
@Component
@Scope(scopeName = SCOPE_PROTOTYPE)
@Initialize(HeatFlowInitializer.class)
public class HeatFlow extends AbstractReportUpdaterComponent<VBox>
        implements HeatFlowReportContainer, ReportInclusionChanger,
        Resettable, ReportDataCollector, Refreshable, PresetChanger,
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
    private final HeatFlowExcelReportsBuilder excelReportsBuilder;
    private final HeatFlowPresetApplier heatFlowPresetApplier;

    @Override
    public void postConstruct() {
        excludeReportIfNeeded();
    }

    private void excludeReportIfNeeded() {
        if (isReportExcluded()) {
            excludeReport();
        }
    }

    private boolean isReportExcluded() {
        return !generalProperties.getIncludedReports().contains(HEAT_FLOW);
    }

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
        Platform.runLater(() -> heatFlowPresetApplier.apply(this, preset));
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
        getSample().removeReport(report);
        disableTab(((SampleTab) getParent()).getHeatFlowTab());
        generalProperties.getIncludedReports().removeIf(HEAT_FLOW::equals);
    }

    @Override
    public void includeReport() {
        val parent = ((SampleTab) getParent());

        getSample().addReport(report);
        enableTab(parent.getHeatFlowTab(), parent.getReportsTabPane());
        generalProperties.getIncludedReports().add(HEAT_FLOW);
    }

    @Override
    public DataViewer getReportData() {
        val excelReports = excelReportsBuilder.build(singletonList(report));

        return new DataViewer(excelReports.get(0));
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
}

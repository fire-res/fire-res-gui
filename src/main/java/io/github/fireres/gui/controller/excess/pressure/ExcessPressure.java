package io.github.fireres.gui.controller.excess.pressure;

import io.github.fireres.core.model.Sample;
import io.github.fireres.core.properties.GeneralProperties;
import io.github.fireres.excel.report.ExcessPressureExcelReportsBuilder;
import io.github.fireres.excess.pressure.properties.ExcessPressureProperties;
import io.github.fireres.excess.pressure.report.ExcessPressureReport;
import io.github.fireres.excess.pressure.service.ExcessPressureService;
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
import io.github.fireres.gui.controller.common.ReportToolBar;
import io.github.fireres.gui.controller.common.SampleTab;
import io.github.fireres.gui.initializer.report.ExcessPressureInitializer;
import io.github.fireres.gui.preset.Preset;
import io.github.fireres.gui.preset.impl.ExcessPressurePresetApplier;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.val;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static io.github.fireres.core.properties.ReportType.EXCESS_PRESSURE;
import static io.github.fireres.gui.util.TabUtils.disableTab;
import static io.github.fireres.gui.util.TabUtils.enableTab;
import static java.util.Collections.singletonList;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@FxmlView("excessPressure.fxml")
@RequiredArgsConstructor
@Component
@Scope(scopeName = SCOPE_PROTOTYPE)
@Initialize(ExcessPressureInitializer.class)
public class ExcessPressure extends AbstractReportUpdaterComponent<VBox>
        implements ExcessPressureReportContainer, ReportInclusionChanger,
        Resettable, ReportDataCollector, Refreshable, PresetChanger,
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
    private final ExcessPressureExcelReportsBuilder excelReportsBuilder;
    private final ExcessPressurePresetApplier excessPressurePresetApplier;

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
        return !generalProperties.getIncludedReports().contains(EXCESS_PRESSURE);
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
        Platform.runLater(() -> excessPressurePresetApplier.apply(this, preset));
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
        getSample().removeReport(report);
        disableTab(((SampleTab) getParent()).getExcessPressureTab());
        generalProperties.getIncludedReports().removeIf(EXCESS_PRESSURE::equals);
    }

    @Override
    public void includeReport() {
        val parent = (SampleTab) getParent();

        getSample().addReport(report);
        enableTab(parent.getExcessPressureTab(), parent.getReportsTabPane());
        generalProperties.getIncludedReports().add(EXCESS_PRESSURE);
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

}

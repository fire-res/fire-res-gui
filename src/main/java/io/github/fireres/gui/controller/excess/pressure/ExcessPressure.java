package io.github.fireres.gui.controller.excess.pressure;

import io.github.fireres.core.model.DoublePoint;
import io.github.fireres.core.model.Sample;
import io.github.fireres.core.properties.GenerationProperties;
import io.github.fireres.excel.report.ExcessPressureExcelReportsBuilder;
import io.github.fireres.excess.pressure.properties.ExcessPressureProperties;
import io.github.fireres.excess.pressure.report.ExcessPressureReport;
import io.github.fireres.excess.pressure.service.ExcessPressureService;
import io.github.fireres.gui.component.DataViewer;
import io.github.fireres.gui.configurer.report.ExcessPressureParametersConfigurer;
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
import io.github.fireres.gui.model.ReportTask;
import io.github.fireres.gui.preset.Preset;
import io.github.fireres.gui.service.ReportExecutorService;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import io.github.fireres.gui.annotation.FxmlView;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static io.github.fireres.core.properties.ReportType.EXCESS_PRESSURE;
import static io.github.fireres.gui.synchronizer.impl.ExcessPressureChartSynchronizer.MAX_ALLOWED_PRESSURE_TEXT;
import static io.github.fireres.gui.synchronizer.impl.ExcessPressureChartSynchronizer.MIN_ALLOWED_PRESSURE_TEXT;
import static io.github.fireres.gui.util.TabUtils.disableTab;
import static io.github.fireres.gui.util.TabUtils.enableTab;
import static java.util.Collections.singletonList;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@FxmlView("excessPressure.fxml")
@RequiredArgsConstructor
@Component
@Scope(scopeName = SCOPE_PROTOTYPE)
public class ExcessPressure extends AbstractReportUpdaterComponent<VBox>
        implements ExcessPressureReportContainer, ReportInclusionChanger,
        ReportCreator, Resettable, ReportDataCollector, Refreshable, PresetChanger {

    @FXML
    @Getter
    private VBox paramsVbox;

    @Getter
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
    private final GenerationProperties generationProperties;
    private final ReportExecutorService reportExecutorService;
    private final ExcessPressureExcelReportsBuilder excelReportsBuilder;
    private final ExcessPressureParametersConfigurer excessPressureParametersConfigurer;

    @Override
    public Sample getSample() {
        return ((SampleTab) getParent()).getSample();
    }

    @Override
    protected void initialize() {
        getBoundsShiftParams().addBoundShift(
                MAX_ALLOWED_PRESSURE_TEXT,
                singletonList(paramsVbox),
                properties -> ((ExcessPressureProperties) properties).getBoundsShift().getMaxAllowedPressureShift(),
                point -> excessPressureService.addMaxAllowedPressureShift(report, (DoublePoint) point),
                point -> excessPressureService.removeMaxAllowedPressureShift(report, (DoublePoint) point),
                (integer, number) -> new DoublePoint(integer, number.doubleValue())
        );

        getBoundsShiftParams().addBoundShift(
                MIN_ALLOWED_PRESSURE_TEXT,
                singletonList(paramsVbox),
                properties -> ((ExcessPressureProperties) properties).getBoundsShift().getMinAllowedPressureShift(),
                point -> excessPressureService.addMinAllowedPressureShift(report, (DoublePoint) point),
                point -> excessPressureService.removeMinAllowedPressureShift(report, (DoublePoint) point),
                (integer, number) -> new DoublePoint(integer, number.doubleValue())
        );
    }

    @Override
    public void createReport() {
        val reportId = UUID.randomUUID();

        val task = ReportTask.builder()
                .updatingElementId(reportId)
                .chartContainers(singletonList(getChartContainer()))
                .nodesToLock(singletonList(paramsVbox))
                .action(() -> {
                    this.report = excessPressureService.createReport(reportId, getSample());

                    if (!generationProperties.getGeneral().getIncludedReports().contains(EXCESS_PRESSURE)) {
                        excludeReport();
                    }
                })
                .build();

        reportExecutorService.runTask(task);
    }

    @Override
    public void postConstruct() {
        excessPressureParametersConfigurer.config(this,
                ((PresetContainer) getParent()).getPreset());
    }

    @Override
    public void refresh() {
        createReport();
    }

    @Override
    public void reset() {
        updateReport(() -> {
            changePreset(((PresetContainer) getParent()).getPreset());
            refresh();
        }, getParamsVbox());
    }

    @Override
    public void changePreset(Preset preset) {
        excessPressureParametersConfigurer.config(this, preset);

        refresh();
    }

    @Override
    public void excludeReport() {
        getSample().removeReport(report);
        disableTab(((SampleTab) getParent()).getExcessPressureTab());
        generationProperties.getGeneral().getIncludedReports().removeIf(EXCESS_PRESSURE::equals);
    }

    @Override
    public void includeReport() {
        val parent = (SampleTab) getParent();

        getSample().putReport(report);
        enableTab(parent.getExcessPressureTab(), parent.getReportsTabPane());
        generationProperties.getGeneral().getIncludedReports().add(EXCESS_PRESSURE);
    }

    @Override
    public DataViewer getReportData() {
        val excelReports = excelReportsBuilder.build(
                generationProperties.getGeneral(), singletonList(report));

        if (excelReports.size() != 1) {
            throw new IllegalStateException();
        }

        return new DataViewer(excelReports.get(0));
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
    public UUID getUpdatingElementId() {
        return getReport().getId();
    }

}

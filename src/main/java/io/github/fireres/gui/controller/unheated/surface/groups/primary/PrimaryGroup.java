package io.github.fireres.gui.controller.unheated.surface.groups.primary;

import io.github.fireres.core.model.Sample;
import io.github.fireres.excel.report.UnheatedSurfaceExcelReportsBuilder;
import io.github.fireres.gui.annotation.FxmlView;
import io.github.fireres.gui.annotation.Initialize;
import io.github.fireres.gui.component.DataViewer;
import io.github.fireres.gui.controller.AbstractReportUpdaterComponent;
import io.github.fireres.gui.controller.ChartContainer;
import io.github.fireres.gui.controller.PresetContainer;
import io.github.fireres.gui.controller.Refreshable;
import io.github.fireres.gui.controller.ReportCreator;
import io.github.fireres.gui.controller.ReportDataCollector;
import io.github.fireres.gui.controller.Resettable;
import io.github.fireres.gui.controller.common.BoundsShiftParams;
import io.github.fireres.gui.controller.common.FunctionParams;
import io.github.fireres.gui.controller.common.ReportToolBar;
import io.github.fireres.gui.controller.unheated.surface.UnheatedSurface;
import io.github.fireres.gui.controller.unheated.surface.UnheatedSurfaceReportContainer;
import io.github.fireres.gui.initializer.report.UnheatedSurfacePrimaryGroupInitializer;
import io.github.fireres.gui.preset.impl.UnheatedSurfacePrimaryGroupPresetApplier;
import io.github.fireres.unheated.surface.properties.UnheatedSurfaceProperties;
import io.github.fireres.unheated.surface.report.UnheatedSurfaceReport;
import io.github.fireres.unheated.surface.service.UnheatedSurfaceService;
import javafx.fxml.FXML;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.val;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static java.util.Collections.singletonList;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@FxmlView("primaryGroup.fxml")
@RequiredArgsConstructor
@Component
@Scope(scopeName = SCOPE_PROTOTYPE)
@Initialize(UnheatedSurfacePrimaryGroupInitializer.class)
public class PrimaryGroup extends AbstractReportUpdaterComponent<TitledPane> implements
        UnheatedSurfaceReportContainer, ReportDataCollector, Resettable, Refreshable,
        ReportCreator<UnheatedSurfaceProperties> {

    @FXML
    @Getter
    private VBox paramsVbox;

    @Setter
    @Getter
    private UUID propertiesId;

    @FXML
    private PrimaryGroupParams primaryGroupParamsController;

    @FXML
    private FunctionParams functionParamsController;

    @FXML
    private PrimaryGroupChart primaryGroupChartController;

    @FXML
    private BoundsShiftParams boundsShiftParamsController;

    @FXML
    private ReportToolBar toolBarController;

    @Getter
    private UnheatedSurfaceReport report;

    private final UnheatedSurfaceService unheatedSurfaceService;
    private final UnheatedSurfaceExcelReportsBuilder excelReportsBuilder;
    private final UnheatedSurfacePrimaryGroupPresetApplier presetApplier;

    @Override
    public void refresh() {
        createReport(report.getProperties());
    }

    @Override
    public void reset() {
        updateReport(this::reapplyPreset, getParamsVbox());
    }

    private void reapplyPreset() {
        presetApplier.apply(this, ((PresetContainer) getParent().getParent()).getPreset());
    }

    @Override
    public void createReport(UnheatedSurfaceProperties properties) {
        updateReport(
                () -> {
                    getSample().removeReport(report);
                    this.report = unheatedSurfaceService.createReport(getSample(), properties);
                },
                getParamsVbox());
    }

    @Override
    public DataViewer getReportData() {
        val excelReports = excelReportsBuilder.build(singletonList(getReport()));

        return new DataViewer(excelReports.get(0));
    }

    @Override
    public Sample getSample() {
        return ((UnheatedSurface) getParent()).getSample();
    }

    @Override
    public ChartContainer getChartContainer() {
        return primaryGroupChartController;
    }

    @Override
    public UUID getReportId() {
        return report == null ? null : report.getId();
    }

    public PrimaryGroupParams getFirstGroupParams() {
        return primaryGroupParamsController;
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
}

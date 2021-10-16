package io.github.fireres.gui.controller.unheated.surface.groups.first;

import io.github.fireres.core.model.Sample;
import io.github.fireres.core.properties.GenerationProperties;
import io.github.fireres.excel.report.UnheatedSurfaceExcelReportsBuilder;
import io.github.fireres.gui.annotation.Initialize;
import io.github.fireres.gui.annotation.FxmlView;
import io.github.fireres.gui.component.DataViewer;
import io.github.fireres.gui.initializer.report.UnheatedSurfaceFirstGroupInitializer;
import io.github.fireres.gui.preset.impl.UnheatedSurfaceFirstGroupPresetApplier;
import io.github.fireres.gui.controller.AbstractReportUpdaterComponent;
import io.github.fireres.gui.controller.ChartContainer;
import io.github.fireres.gui.controller.PresetChanger;
import io.github.fireres.gui.controller.PresetContainer;
import io.github.fireres.gui.controller.Refreshable;
import io.github.fireres.gui.controller.ReportDataCollector;
import io.github.fireres.gui.controller.Resettable;
import io.github.fireres.gui.controller.common.BoundsShiftParams;
import io.github.fireres.gui.controller.common.FunctionParams;
import io.github.fireres.gui.controller.common.ReportToolBar;
import io.github.fireres.gui.controller.unheated.surface.UnheatedSurface;
import io.github.fireres.gui.controller.unheated.surface.UnheatedSurfaceReportContainer;
import io.github.fireres.gui.preset.Preset;
import io.github.fireres.unheated.surface.report.UnheatedSurfaceReport;
import io.github.fireres.unheated.surface.service.UnheatedSurfaceFirstGroupService;
import javafx.fxml.FXML;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static java.util.Collections.singletonList;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@FxmlView("firstGroup.fxml")
@RequiredArgsConstructor
@Component
@Scope(scopeName = SCOPE_PROTOTYPE)
@Initialize(UnheatedSurfaceFirstGroupInitializer.class)
public class FirstGroup extends AbstractReportUpdaterComponent<TitledPane>
        implements UnheatedSurfaceReportContainer, ReportDataCollector,
        Resettable, Refreshable, PresetChanger {

    @FXML
    @Getter
    private VBox paramsVbox;

    @FXML
    private FirstGroupParams firstGroupParamsController;

    @FXML
    private FunctionParams functionParamsController;

    @FXML
    private FirstGroupChart firstGroupChartController;

    @FXML
    private BoundsShiftParams boundsShiftParamsController;

    @FXML
    private ReportToolBar toolBarController;

    private final UnheatedSurfaceFirstGroupService unheatedSurfaceFirstGroupService;
    private final UnheatedSurfaceExcelReportsBuilder excelReportsBuilder;
    private final GenerationProperties generationProperties;
    private final UnheatedSurfaceFirstGroupPresetApplier firstGroupConfigurer;

    @Override
    public void refresh() {
        updateReport(() -> unheatedSurfaceFirstGroupService.refreshFirstGroup(getReport()));
    }

    @Override
    public void reset() {
        updateReport(() -> {
            changePreset(((PresetContainer) getParent().getParent()).getPreset());
            unheatedSurfaceFirstGroupService.refreshFirstGroup(getReport());
        }, getParamsVbox());
    }

    @Override
    public void changePreset(Preset preset) {
        firstGroupConfigurer.apply(this, preset);
    }

    @Override
    public DataViewer getReportData() {
        val excelReports = excelReportsBuilder.build(
                generationProperties.getGeneral(), singletonList(getReport()));

        if (excelReports.size() != 3) {
            throw new IllegalStateException();
        }

        return new DataViewer(excelReports.get(0));
    }

    @Override
    public Sample getSample() {
        return ((UnheatedSurface) getParent()).getSample();
    }

    @Override
    public UnheatedSurfaceReport getReport() {
        return ((UnheatedSurface) getParent()).getReport();
    }

    @Override
    public ChartContainer getChartContainer() {
        return firstGroupChartController;
    }

    @Override
    public UUID getUpdatingElementId() {
        return getReport().getFirstGroup().getId();
    }

    public FirstGroupParams getFirstGroupParams() {
        return firstGroupParamsController;
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

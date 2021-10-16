package io.github.fireres.gui.controller.unheated.surface.groups.second;

import io.github.fireres.core.model.Sample;
import io.github.fireres.core.properties.GenerationProperties;
import io.github.fireres.excel.report.UnheatedSurfaceExcelReportsBuilder;
import io.github.fireres.gui.annotation.Initialize;
import io.github.fireres.gui.component.DataViewer;
import io.github.fireres.gui.initializer.report.UnheatedSurfaceSecondGroupInitializer;
import io.github.fireres.gui.preset.impl.UnheatedSurfaceSecondGroupPresetApplier;
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
import io.github.fireres.unheated.surface.service.UnheatedSurfaceSecondGroupService;
import javafx.fxml.FXML;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import io.github.fireres.gui.annotation.FxmlView;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static java.util.Collections.singletonList;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@FxmlView("secondGroup.fxml")
@RequiredArgsConstructor
@Component
@Scope(scopeName = SCOPE_PROTOTYPE)
@Initialize(UnheatedSurfaceSecondGroupInitializer.class)
public class SecondGroup extends AbstractReportUpdaterComponent<TitledPane>
        implements UnheatedSurfaceReportContainer, Resettable,
        ReportDataCollector, Refreshable, PresetChanger {

    @FXML
    @Getter
    private VBox paramsVbox;

    @FXML
    private SecondGroupParams secondGroupParamsController;

    @FXML
    private FunctionParams functionParamsController;

    @FXML
    private SecondGroupChart secondGroupChartController;

    @FXML
    private BoundsShiftParams boundsShiftParamsController;

    @FXML
    private ReportToolBar toolBarController;

    private final UnheatedSurfaceSecondGroupService unheatedSurfaceSecondGroupService;
    private final UnheatedSurfaceExcelReportsBuilder excelReportsBuilder;
    private final GenerationProperties generationProperties;
    private final UnheatedSurfaceSecondGroupPresetApplier secondGroupConfigurer;

    @Override
    public void refresh() {
        updateReport(() -> unheatedSurfaceSecondGroupService.refreshSecondGroup(getReport()));
    }

    @Override
    public void reset() {
        updateReport(() -> {
            changePreset(((PresetContainer) getParent().getParent()).getPreset());
            unheatedSurfaceSecondGroupService.refreshSecondGroup(getReport());
        }, getParamsVbox());
    }

    @Override
    public void changePreset(Preset preset) {
        secondGroupConfigurer.apply(this, preset);
    }

    @Override
    public DataViewer getReportData() {
        val excelReports = excelReportsBuilder.build(
                generationProperties.getGeneral(), singletonList(getReport()));

        if (excelReports.size() != 3) {
            throw new IllegalStateException();
        }

        return new DataViewer(excelReports.get(1));
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
        return secondGroupChartController;
    }

    @Override
    public UUID getUpdatingElementId() {
        return getReport().getSecondGroup().getId();
    }

    public SecondGroupParams getSecondGroupParams() {
        return secondGroupParamsController;
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

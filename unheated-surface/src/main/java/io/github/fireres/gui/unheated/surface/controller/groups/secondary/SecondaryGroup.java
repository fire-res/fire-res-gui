package io.github.fireres.gui.unheated.surface.controller.groups.secondary;

import io.github.fireres.core.model.Sample;
import io.github.fireres.gui.framework.annotation.FxmlView;
import io.github.fireres.gui.framework.annotation.Initialize;
import io.github.fireres.gui.framework.controller.AbstractReportUpdaterComponent;
import io.github.fireres.gui.framework.controller.ChartContainer;
import io.github.fireres.gui.framework.controller.PresetContainer;
import io.github.fireres.gui.framework.controller.Refreshable;
import io.github.fireres.gui.framework.controller.ReportCreator;
import io.github.fireres.gui.framework.controller.ReportDataCollector;
import io.github.fireres.gui.framework.controller.Resettable;
import io.github.fireres.gui.framework.controller.common.BoundsShiftParams;
import io.github.fireres.gui.framework.controller.common.FunctionParams;
import io.github.fireres.gui.framework.controller.common.ReportToolBar;
import io.github.fireres.gui.framework.service.DataViewService;
import io.github.fireres.gui.unheated.surface.controller.UnheatedSurface;
import io.github.fireres.gui.unheated.surface.controller.UnheatedSurfaceReportContainer;
import io.github.fireres.gui.unheated.surface.initializer.UnheatedSurfaceSecondaryGroupInitializer;
import io.github.fireres.gui.unheated.surface.preset.UnheatedSurfaceSecondaryGroupPresetApplier;
import io.github.fireres.unheated.surface.properties.UnheatedSurfaceProperties;
import io.github.fireres.unheated.surface.report.UnheatedSurfaceReport;
import io.github.fireres.unheated.surface.service.UnheatedSurfaceService;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@FxmlView("secondaryGroup.fxml")
@RequiredArgsConstructor
@Component
@Scope(scopeName = SCOPE_PROTOTYPE)
@Initialize(UnheatedSurfaceSecondaryGroupInitializer.class)
public class SecondaryGroup extends AbstractReportUpdaterComponent<TitledPane> implements
        UnheatedSurfaceReportContainer, Resettable, ReportDataCollector, Refreshable,
        ReportCreator<UnheatedSurfaceProperties> {

    @FXML
    @Getter
    private VBox paramsVbox;

    @Setter
    @Getter
    private UUID propertiesId;

    @FXML
    private SecondaryGroupParams secondaryGroupParamsController;

    @FXML
    private FunctionParams functionParamsController;

    @FXML
    private SecondaryGroupChart secondaryGroupChartController;

    @FXML
    private BoundsShiftParams boundsShiftParamsController;

    @FXML
    private ReportToolBar toolBarController;

    @Getter
    private UnheatedSurfaceReport report;

    private final UnheatedSurfaceService unheatedSurfaceService;
    private final DataViewService dataViewService;
    private final UnheatedSurfaceSecondaryGroupPresetApplier presetApplier;

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

    @FXML
    public void handleExpandChanged() {
        if (getComponent().isExpanded()) {
            ((UnheatedSurface) getParent()).collapseGroupsExcept(this);
        }
    }

    @Override
    public TableView<Map<String, Number>> getReportData() {
        return dataViewService.getDataViewer(report);
    }

    @Override
    public Sample getSample() {
        return ((UnheatedSurface) getParent()).getSample();
    }

    @Override
    public ChartContainer getChartContainer() {
        return secondaryGroupChartController;
    }

    @Override
    public UUID getReportId() {
        return report == null ? null : report.getId();
    }

    public SecondaryGroupParams getSecondGroupParams() {
        return secondaryGroupParamsController;
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

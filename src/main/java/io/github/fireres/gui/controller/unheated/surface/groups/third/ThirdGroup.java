package io.github.fireres.gui.controller.unheated.surface.groups.third;

import io.github.fireres.core.model.IntegerPoint;
import io.github.fireres.core.model.Sample;
import io.github.fireres.core.properties.GenerationProperties;
import io.github.fireres.excel.report.UnheatedSurfaceExcelReportsBuilder;
import io.github.fireres.gui.component.DataViewer;
import io.github.fireres.gui.configurer.report.UnheatedSurfaceThirdGroupConfigurer;
import io.github.fireres.gui.controller.AbstractReportUpdaterComponent;
import io.github.fireres.gui.controller.ChartContainer;
import io.github.fireres.gui.controller.PresetContainer;
import io.github.fireres.gui.controller.Refreshable;
import io.github.fireres.gui.controller.ReportDataCollector;
import io.github.fireres.gui.controller.Resettable;
import io.github.fireres.gui.controller.common.BoundsShiftParams;
import io.github.fireres.gui.controller.common.FunctionParams;
import io.github.fireres.gui.controller.common.ReportToolBar;
import io.github.fireres.gui.controller.unheated.surface.UnheatedSurface;
import io.github.fireres.gui.controller.unheated.surface.UnheatedSurfaceReportContainer;
import io.github.fireres.unheated.surface.properties.UnheatedSurfaceProperties;
import io.github.fireres.unheated.surface.report.UnheatedSurfaceReport;
import io.github.fireres.unheated.surface.service.UnheatedSurfaceThirdGroupService;
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

import static io.github.fireres.gui.synchronizer.impl.ThirdThermocoupleGroupChartSynchronizer.MAX_THERMOCOUPLE_TEMPERATURE_TEXT;
import static java.util.Collections.singletonList;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@FxmlView("thirdGroup.fxml")
@RequiredArgsConstructor
@Component
@Scope(scopeName = SCOPE_PROTOTYPE)
public class ThirdGroup extends AbstractReportUpdaterComponent<TitledPane>
        implements UnheatedSurfaceReportContainer, Resettable, ReportDataCollector, Refreshable {

    @FXML
    @Getter
    private VBox paramsVbox;

    @FXML
    private ThirdGroupParams thirdGroupParamsController;

    @FXML
    private ThirdGroupChart thirdGroupChartController;

    @FXML
    private FunctionParams functionParamsController;

    @FXML
    private BoundsShiftParams boundsShiftParamsController;

    @FXML
    private ReportToolBar toolBarController;

    private final UnheatedSurfaceThirdGroupService unheatedSurfaceThirdGroupService;
    private final UnheatedSurfaceExcelReportsBuilder excelReportsBuilder;
    private final GenerationProperties generationProperties;
    private final UnheatedSurfaceThirdGroupConfigurer thirdGroupConfigurer;

    @Override
    public Sample getSample() {
        return ((UnheatedSurface) getParent()).getSample();
    }

    @Override
    protected void initialize() {
        initializeFunctionParams();
        initializeBoundsShiftParams();
    }

    private void initializeFunctionParams() {
        getFunctionParams().setInterpolationService(unheatedSurfaceThirdGroupService);

        getFunctionParams().setPropertiesMapper(props ->
                props.getReportPropertiesByClass(UnheatedSurfaceProperties.class)
                        .orElseThrow()
                        .getThirdGroup()
                        .getFunctionForm());

        getFunctionParams().setNodesToBlockOnUpdate(singletonList(paramsVbox));

        getFunctionParams().setInterpolationPointConstructor((time, value) -> new IntegerPoint(time, value.intValue()));
    }

    private void initializeBoundsShiftParams() {
        getBoundsShiftParams().addBoundShift(
                MAX_THERMOCOUPLE_TEMPERATURE_TEXT,
                singletonList(paramsVbox),
                properties -> ((UnheatedSurfaceProperties) properties).getThirdGroup().getBoundsShift().getMaxAllowedTemperatureShift(),
                point -> unheatedSurfaceThirdGroupService.addMaxAllowedTemperatureShift(getReport(), (IntegerPoint) point),
                point -> unheatedSurfaceThirdGroupService.removeMaxAllowedTemperatureShift(getReport(), (IntegerPoint) point),
                (integer, number) -> new IntegerPoint(integer, number.intValue())
        );
    }

    @Override
    public void refresh() {
        updateReport(() -> unheatedSurfaceThirdGroupService.refreshThirdGroup(getReport()));
    }

    @Override
    public void reset() {
        updateReport(() -> {
            thirdGroupConfigurer.config(this,
                    ((PresetContainer) getParent().getParent()).getPreset());

            unheatedSurfaceThirdGroupService.refreshThirdGroup(getReport());
        }, getParamsVbox());
    }

    @Override
    public DataViewer getReportData() {
        val excelReports = excelReportsBuilder.build(
                generationProperties.getGeneral(), singletonList(getReport()));

        if (excelReports.size() != 3) {
            throw new IllegalStateException();
        }

        return new DataViewer(excelReports.get(2));
    }

    @Override
    public UnheatedSurfaceReport getReport() {
        return ((UnheatedSurface) getParent()).getReport();
    }

    @Override
    public ChartContainer getChartContainer() {
        return thirdGroupChartController;
    }

    @Override
    public UUID getUpdatingElementId() {
        return getReport().getThirdGroup().getId();
    }

    public ThirdGroupParams getThirdGroupParams() {
        return thirdGroupParamsController;
    }

    public FunctionParams getFunctionParams() {
        return functionParamsController;
    }

    public BoundsShiftParams getBoundsShiftParams() {
        return boundsShiftParamsController;
    }
}

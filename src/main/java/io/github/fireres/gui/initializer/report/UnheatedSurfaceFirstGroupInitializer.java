package io.github.fireres.gui.initializer.report;

import io.github.fireres.core.model.IntegerPoint;
import io.github.fireres.gui.controller.PresetContainer;
import io.github.fireres.gui.controller.unheated.surface.groups.first.FirstGroup;
import io.github.fireres.gui.initializer.Initializer;
import io.github.fireres.gui.preset.impl.UnheatedSurfaceFirstGroupPresetApplier;
import io.github.fireres.unheated.surface.properties.UnheatedSurfaceProperties;
import io.github.fireres.unheated.surface.service.UnheatedSurfaceFirstGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static io.github.fireres.gui.synchronizer.impl.FirstThermocoupleGroupChartSynchronizer.MAX_MEAN_TEMPERATURE_TEXT;
import static io.github.fireres.gui.synchronizer.impl.FirstThermocoupleGroupChartSynchronizer.MAX_THERMOCOUPLE_TEMPERATURE_TEXT;
import static java.util.Collections.singletonList;

@Component
@RequiredArgsConstructor
public class UnheatedSurfaceFirstGroupInitializer implements Initializer<FirstGroup> {

    private final UnheatedSurfaceFirstGroupPresetApplier presetApplier;
    private final UnheatedSurfaceFirstGroupService unheatedSurfaceFirstGroupService;

    @Override
    public void initialize(FirstGroup firstGroup) {
        applyDefaultPreset(firstGroup);
        initializeFunctionParams(firstGroup);
        initializeBoundsShiftParams(firstGroup);
    }

    private void applyDefaultPreset(FirstGroup firstGroup) {
        presetApplier.apply(firstGroup, ((PresetContainer) firstGroup.getParent().getParent()).getPreset());
    }

    private void initializeFunctionParams(FirstGroup firstGroup) {
        firstGroup.getFunctionParams().setInterpolationService(unheatedSurfaceFirstGroupService);

        firstGroup.getFunctionParams().setPropertiesMapper(props ->
                props.getReportPropertiesByClass(UnheatedSurfaceProperties.class)
                        .orElseThrow()
                        .getFirstGroup()
                        .getFunctionForm());

        firstGroup.getFunctionParams().setNodesToBlockOnUpdate(singletonList(firstGroup.getParamsVbox()));

        firstGroup.getFunctionParams().setInterpolationPointConstructor(
                (time, value) -> new IntegerPoint(time, value.intValue()));
    }

    private void initializeBoundsShiftParams(FirstGroup firstGroup) {
        firstGroup.getBoundsShiftParams().addBoundShift(
                MAX_MEAN_TEMPERATURE_TEXT,
                singletonList(firstGroup.getParamsVbox()),
                properties -> ((UnheatedSurfaceProperties) properties).getFirstGroup().getBoundsShift().getMaxAllowedMeanTemperatureShift(),
                point -> unheatedSurfaceFirstGroupService.addMaxAllowedMeanTemperatureShift(firstGroup.getReport(), (IntegerPoint) point),
                point -> unheatedSurfaceFirstGroupService.removeMaxAllowedMeanTemperatureShift(firstGroup.getReport(), (IntegerPoint) point),
                (integer, number) -> new IntegerPoint(integer, number.intValue())
        );

        firstGroup.getBoundsShiftParams().addBoundShift(
                MAX_THERMOCOUPLE_TEMPERATURE_TEXT,
                singletonList(firstGroup.getParamsVbox()),
                properties -> ((UnheatedSurfaceProperties) properties).getFirstGroup().getBoundsShift().getMaxAllowedThermocoupleTemperatureShift(),
                point -> unheatedSurfaceFirstGroupService.addMaxAllowedThermocoupleTemperatureShift(firstGroup.getReport(), (IntegerPoint) point),
                point -> unheatedSurfaceFirstGroupService.removeMaxAllowedThermocoupleTemperatureShift(firstGroup.getReport(), (IntegerPoint) point),
                (integer, number) -> new IntegerPoint(integer, number.intValue())
        );
    }

}

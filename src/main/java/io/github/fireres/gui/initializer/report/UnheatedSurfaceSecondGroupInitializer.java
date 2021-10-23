package io.github.fireres.gui.initializer.report;

import io.github.fireres.core.model.IntegerPoint;
import io.github.fireres.gui.controller.PresetContainer;
import io.github.fireres.gui.controller.unheated.surface.groups.second.SecondGroup;
import io.github.fireres.gui.initializer.Initializer;
import io.github.fireres.gui.preset.impl.UnheatedSurfaceSecondGroupPresetApplier;
import io.github.fireres.unheated.surface.properties.UnheatedSurfaceProperties;
import io.github.fireres.unheated.surface.service.UnheatedSurfaceSecondGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static io.github.fireres.gui.synchronizer.impl.SecondThermocoupleGroupChartSynchronizer.MAX_THERMOCOUPLE_TEMPERATURE_TEXT;
import static java.util.Collections.singletonList;

@Component
@RequiredArgsConstructor
public class UnheatedSurfaceSecondGroupInitializer implements Initializer<SecondGroup> {

    private final UnheatedSurfaceSecondGroupPresetApplier presetApplier;
    private final UnheatedSurfaceSecondGroupService unheatedSurfaceSecondGroupService;

    @Override
    public void initialize(SecondGroup secondGroup) {
        applyDefaultPreset(secondGroup);
        initializeFunctionParams(secondGroup);
        initializeBoundsShiftParams(secondGroup);
    }

    private void applyDefaultPreset(SecondGroup secondGroup) {
        presetApplier.apply(secondGroup, ((PresetContainer) secondGroup.getParent().getParent()).getPreset());
    }

    private void initializeFunctionParams(SecondGroup secondGroup) {
        secondGroup.getFunctionParams().setInterpolationService(unheatedSurfaceSecondGroupService);

        secondGroup.getFunctionParams().setPropertiesMapper(props ->
                props.getReportPropertiesByClass(UnheatedSurfaceProperties.class)
                        .orElseThrow()
                        .getSecondGroup()
                        .getFunctionForm());

        secondGroup.getFunctionParams().setNodesToBlockOnUpdate(singletonList(secondGroup.getParamsVbox()));

        secondGroup.getFunctionParams().setInterpolationPointConstructor(
                (time, value) -> new IntegerPoint(time, value.intValue()));
    }

    private void initializeBoundsShiftParams(SecondGroup secondGroup) {
        secondGroup.getBoundsShiftParams().addBoundShift(
                MAX_THERMOCOUPLE_TEMPERATURE_TEXT,
                singletonList(secondGroup.getParamsVbox()),
                properties -> ((UnheatedSurfaceProperties) properties).getSecondGroup().getBoundsShift().getMaxAllowedTemperatureShift(),
                point -> unheatedSurfaceSecondGroupService.addMaxAllowedTemperatureShift(secondGroup.getReport(), (IntegerPoint) point),
                point -> unheatedSurfaceSecondGroupService.removeMaxAllowedTemperatureShift(secondGroup.getReport(), (IntegerPoint) point),
                (integer, number) -> new IntegerPoint(integer, number.intValue())
        );
    }

}

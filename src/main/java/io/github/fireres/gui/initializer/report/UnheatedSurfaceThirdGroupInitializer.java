package io.github.fireres.gui.initializer.report;

import io.github.fireres.core.model.IntegerPoint;
import io.github.fireres.gui.controller.PresetContainer;
import io.github.fireres.gui.controller.unheated.surface.groups.third.ThirdGroup;
import io.github.fireres.gui.initializer.Initializer;
import io.github.fireres.gui.preset.impl.UnheatedSurfaceThirdGroupPresetApplier;
import io.github.fireres.unheated.surface.properties.UnheatedSurfaceProperties;
import io.github.fireres.unheated.surface.service.UnheatedSurfaceThirdGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static io.github.fireres.gui.synchronizer.impl.ThirdThermocoupleGroupChartSynchronizer.MAX_THERMOCOUPLE_TEMPERATURE_TEXT;
import static java.util.Collections.singletonList;

@Component
@RequiredArgsConstructor
public class UnheatedSurfaceThirdGroupInitializer implements Initializer<ThirdGroup> {

    private final UnheatedSurfaceThirdGroupPresetApplier presetApplier;
    private final UnheatedSurfaceThirdGroupService unheatedSurfaceThirdGroupService;

    @Override
    public void initialize(ThirdGroup thirdGroup) {
        applyDefaultPreset(thirdGroup);
        initializeFunctionParams(thirdGroup);
        initializeBoundsShiftParams(thirdGroup);
    }

    private void applyDefaultPreset(ThirdGroup thirdGroup) {
        presetApplier.apply(thirdGroup, ((PresetContainer) thirdGroup.getParent().getParent()).getPreset());
    }

    private void initializeFunctionParams(ThirdGroup thirdGroup) {
        thirdGroup.getFunctionParams().setInterpolationService(unheatedSurfaceThirdGroupService);

        thirdGroup.getFunctionParams().setPropertiesMapper(props ->
                props.getReportPropertiesByClass(UnheatedSurfaceProperties.class)
                        .orElseThrow()
                        .getThirdGroup()
                        .getFunctionForm());

        thirdGroup.getFunctionParams().setNodesToBlockOnUpdate(singletonList(thirdGroup.getParamsVbox()));

        thirdGroup.getFunctionParams().setInterpolationPointConstructor(
                (time, value) -> new IntegerPoint(time, value.intValue()));
    }

    private void initializeBoundsShiftParams(ThirdGroup thirdGroup) {
        thirdGroup.getBoundsShiftParams().addBoundShift(
                MAX_THERMOCOUPLE_TEMPERATURE_TEXT,
                singletonList(thirdGroup.getParamsVbox()),
                properties -> ((UnheatedSurfaceProperties) properties).getThirdGroup().getBoundsShift().getMaxAllowedTemperatureShift(),
                point -> unheatedSurfaceThirdGroupService.addMaxAllowedTemperatureShift(thirdGroup.getReport(), (IntegerPoint) point),
                point -> unheatedSurfaceThirdGroupService.removeMaxAllowedTemperatureShift(thirdGroup.getReport(), (IntegerPoint) point),
                (integer, number) -> new IntegerPoint(integer, number.intValue())
        );
    }

}

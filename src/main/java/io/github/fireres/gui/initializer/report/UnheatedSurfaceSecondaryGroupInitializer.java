package io.github.fireres.gui.initializer.report;

import io.github.fireres.core.model.IntegerPoint;
import io.github.fireres.gui.controller.unheated.surface.groups.secondary.SecondaryGroup;
import io.github.fireres.gui.initializer.Initializer;
import io.github.fireres.unheated.surface.properties.UnheatedSurfaceProperties;
import io.github.fireres.unheated.surface.service.UnheatedSurfaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static io.github.fireres.gui.synchronizer.impl.SecondaryGroupChartSynchronizer.MAX_THERMOCOUPLE_TEMPERATURE_TEXT;
import static java.util.Collections.singletonList;

@Component
@RequiredArgsConstructor
public class UnheatedSurfaceSecondaryGroupInitializer implements Initializer<SecondaryGroup> {

    private final UnheatedSurfaceService unheatedSurfaceService;

    @Override
    public void initialize(SecondaryGroup secondaryGroup) {
        initializeFunctionParams(secondaryGroup);
        initializeBoundsShiftParams(secondaryGroup);
    }

    private void initializeFunctionParams(SecondaryGroup secondaryGroup) {
        secondaryGroup.getFunctionParams().setInterpolationService(unheatedSurfaceService);

        secondaryGroup.getFunctionParams().setNodesToBlockOnUpdate(singletonList(secondaryGroup.getParamsVbox()));

        secondaryGroup.getFunctionParams().setInterpolationPointConstructor(
                (time, value) -> new IntegerPoint(time, value.intValue()));
    }

    private void initializeBoundsShiftParams(SecondaryGroup secondaryGroup) {
        secondaryGroup.getBoundsShiftParams().addBoundShift(
                MAX_THERMOCOUPLE_TEMPERATURE_TEXT,
                singletonList(secondaryGroup.getParamsVbox()),
                properties -> ((UnheatedSurfaceProperties) properties).getBoundsShift().getMaxAllowedThermocoupleTemperatureShift(),
                point -> unheatedSurfaceService.addMaxAllowedThermocoupleTemperatureShift(secondaryGroup.getReport(), (IntegerPoint) point),
                point -> unheatedSurfaceService.removeMaxAllowedThermocoupleTemperatureShift(secondaryGroup.getReport(), (IntegerPoint) point),
                (integer, number) -> new IntegerPoint(integer, number.intValue())
        );
    }

}

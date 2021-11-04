package io.github.fireres.gui.unheated.surface.initializer;

import io.github.fireres.core.model.IntegerPoint;
import io.github.fireres.gui.framework.initializer.Initializer;
import io.github.fireres.gui.unheated.surface.controller.groups.primary.PrimaryGroup;
import io.github.fireres.unheated.surface.properties.UnheatedSurfaceProperties;
import io.github.fireres.unheated.surface.service.UnheatedSurfaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static io.github.fireres.gui.unheated.surface.synchronizer.PrimaryGroupChartSynchronizer.MAX_MEAN_TEMPERATURE_TEXT;
import static io.github.fireres.gui.unheated.surface.synchronizer.PrimaryGroupChartSynchronizer.MAX_THERMOCOUPLE_TEMPERATURE_TEXT;
import static java.util.Collections.singletonList;

@Component
@RequiredArgsConstructor
public class UnheatedSurfacePrimaryGroupInitializer implements Initializer<PrimaryGroup> {

    private final UnheatedSurfaceService unheatedSurfaceService;

    @Override
    public void initialize(PrimaryGroup primaryGroup) {
        initializeFunctionParams(primaryGroup);
        initializeBoundsShiftParams(primaryGroup);
    }

    private void initializeFunctionParams(PrimaryGroup primaryGroup) {
        primaryGroup.getFunctionParams().setInterpolationService(unheatedSurfaceService);

        primaryGroup.getFunctionParams().setNodesToBlockOnUpdate(singletonList(primaryGroup.getParamsVbox()));

        primaryGroup.getFunctionParams().setInterpolationPointConstructor(
                (time, value) -> new IntegerPoint(time, value.intValue()));
    }

    private void initializeBoundsShiftParams(PrimaryGroup primaryGroup) {
        primaryGroup.getBoundsShiftParams().addBoundShift(
                MAX_MEAN_TEMPERATURE_TEXT,
                singletonList(primaryGroup.getParamsVbox()),
                properties -> ((UnheatedSurfaceProperties) properties).getBoundsShift().getMaxAllowedMeanTemperatureShift(),
                point -> unheatedSurfaceService.addMaxAllowedMeanTemperatureShift(primaryGroup.getReport(), (IntegerPoint) point),
                point -> unheatedSurfaceService.removeMaxAllowedMeanTemperatureShift(primaryGroup.getReport(), (IntegerPoint) point),
                (integer, number) -> new IntegerPoint(integer, number.intValue())
        );

        primaryGroup.getBoundsShiftParams().addBoundShift(
                MAX_THERMOCOUPLE_TEMPERATURE_TEXT,
                singletonList(primaryGroup.getParamsVbox()),
                properties -> ((UnheatedSurfaceProperties) properties).getBoundsShift().getMaxAllowedThermocoupleTemperatureShift(),
                point -> unheatedSurfaceService.addMaxAllowedThermocoupleTemperatureShift(primaryGroup.getReport(), (IntegerPoint) point),
                point -> unheatedSurfaceService.removeMaxAllowedThermocoupleTemperatureShift(primaryGroup.getReport(), (IntegerPoint) point),
                (integer, number) -> new IntegerPoint(integer, number.intValue())
        );
    }

}

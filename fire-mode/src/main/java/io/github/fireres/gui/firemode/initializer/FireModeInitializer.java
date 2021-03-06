package io.github.fireres.gui.firemode.initializer;

import io.github.fireres.core.model.IntegerPoint;
import io.github.fireres.firemode.properties.FireModeProperties;
import io.github.fireres.firemode.service.FireModeService;
import io.github.fireres.gui.firemode.controller.FireMode;
import io.github.fireres.gui.firemode.preset.FireModePresetApplier;
import io.github.fireres.gui.framework.controller.PresetContainer;
import io.github.fireres.gui.framework.initializer.Initializer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static io.github.fireres.gui.firemode.synchronizer.FireModeChartSynchronizer.MAX_ALLOWED_TEMPERATURE_TEXT;
import static io.github.fireres.gui.firemode.synchronizer.FireModeChartSynchronizer.MIN_ALLOWED_TEMPERATURE_TEXT;
import static java.util.Collections.singletonList;

@Component
@RequiredArgsConstructor
public class FireModeInitializer implements Initializer<FireMode> {

    private final FireModePresetApplier presetApplier;
    private final FireModeService fireModeService;

    @Override
    public void initialize(FireMode fireMode) {
        applyDefaultPreset(fireMode);
        initializeFunctionParams(fireMode);
        initializeBoundsShiftParams(fireMode);
    }

    private void applyDefaultPreset(FireMode fireMode) {
        presetApplier.apply(fireMode, ((PresetContainer) fireMode.getParent()).getPreset());
    }

    private void initializeFunctionParams(FireMode fireMode) {
        fireMode.getFunctionParams().setInterpolationService(fireModeService);

        fireMode.getFunctionParams().setNodesToBlockOnUpdate(singletonList(fireMode.getParamsVbox()));

        fireMode.getFunctionParams().setInterpolationPointConstructor(
                (time, value) -> new IntegerPoint(time, value.intValue()));
    }

    private void initializeBoundsShiftParams(FireMode fireMode) {
        fireMode.getBoundsShiftParams().addBoundShift(
                MAX_ALLOWED_TEMPERATURE_TEXT,
                singletonList(fireMode.getParamsVbox()),
                properties -> ((FireModeProperties) properties).getBoundsShift().getMaxAllowedTemperatureShift(),
                point -> fireModeService.addMaxAllowedTemperatureShift(fireMode.getReport(), (IntegerPoint) point),
                point -> fireModeService.removeMaxAllowedTemperatureShift(fireMode.getReport(), (IntegerPoint) point),
                (integer, number) -> new IntegerPoint(integer, number.intValue())
        );

        fireMode.getBoundsShiftParams().addBoundShift(
                MIN_ALLOWED_TEMPERATURE_TEXT,
                singletonList(fireMode.getParamsVbox()),
                properties -> ((FireModeProperties) properties).getBoundsShift().getMinAllowedTemperatureShift(),
                point -> fireModeService.addMinAllowedTemperatureShift(fireMode.getReport(), (IntegerPoint) point),
                point -> fireModeService.removeMinAllowedTemperatureShift(fireMode.getReport(), (IntegerPoint) point),
                (integer, number) -> new IntegerPoint(integer, number.intValue())
        );
    }

}

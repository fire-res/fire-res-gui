package io.github.fireres.gui.excess.pressure.initializer;

import io.github.fireres.gui.framework.controller.PresetContainer;
import io.github.fireres.core.model.DoublePoint;
import io.github.fireres.excess.pressure.properties.ExcessPressureProperties;
import io.github.fireres.excess.pressure.service.ExcessPressureService;
import io.github.fireres.gui.framework.initializer.Initializer;
import io.github.fireres.gui.excess.pressure.preset.ExcessPressurePresetApplier;
import io.github.fireres.gui.excess.pressure.controller.ExcessPressure;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static io.github.fireres.gui.excess.pressure.synchronizer.ExcessPressureChartSynchronizer.MAX_ALLOWED_PRESSURE_TEXT;
import static io.github.fireres.gui.excess.pressure.synchronizer.ExcessPressureChartSynchronizer.MIN_ALLOWED_PRESSURE_TEXT;
import static java.util.Collections.singletonList;

@Component
@RequiredArgsConstructor
public class ExcessPressureInitializer implements Initializer<ExcessPressure> {

    private final ExcessPressurePresetApplier presetApplier;
    private final ExcessPressureService excessPressureService;

    @Override
    public void initialize(ExcessPressure excessPressure) {
        applyDefaultPreset(excessPressure);
        initializeBoundShifts(excessPressure);
    }

    private void applyDefaultPreset(ExcessPressure excessPressure) {
        presetApplier.apply(excessPressure, ((PresetContainer) excessPressure.getParent()).getPreset());
    }

    private void initializeBoundShifts(ExcessPressure excessPressure) {
        excessPressure.getBoundsShiftParams().addBoundShift(
                MAX_ALLOWED_PRESSURE_TEXT,
                singletonList(excessPressure.getParamsVbox()),
                properties -> ((ExcessPressureProperties) properties).getBoundsShift().getMaxAllowedPressureShift(),
                point -> excessPressureService.addMaxAllowedPressureShift(excessPressure.getReport(), (DoublePoint) point),
                point -> excessPressureService.removeMaxAllowedPressureShift(excessPressure.getReport(), (DoublePoint) point),
                (integer, number) -> new DoublePoint(integer, number.doubleValue())
        );

        excessPressure.getBoundsShiftParams().addBoundShift(
                MIN_ALLOWED_PRESSURE_TEXT,
                singletonList(excessPressure.getParamsVbox()),
                properties -> ((ExcessPressureProperties) properties).getBoundsShift().getMinAllowedPressureShift(),
                point -> excessPressureService.addMinAllowedPressureShift(excessPressure.getReport(), (DoublePoint) point),
                point -> excessPressureService.removeMinAllowedPressureShift(excessPressure.getReport(), (DoublePoint) point),
                (integer, number) -> new DoublePoint(integer, number.doubleValue())
        );
    }

}

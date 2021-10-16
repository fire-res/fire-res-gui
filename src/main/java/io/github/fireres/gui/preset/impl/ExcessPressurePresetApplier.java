package io.github.fireres.gui.preset.impl;

import com.rits.cloning.Cloner;
import io.github.fireres.excess.pressure.properties.ExcessPressureProperties;
import io.github.fireres.gui.config.properties.excess.pressure.ExcessPressureBasePressureProperties;
import io.github.fireres.gui.config.properties.excess.pressure.ExcessPressureDeltaProperties;
import io.github.fireres.gui.config.properties.excess.pressure.ExcessPressureDispersionCoefficientProperties;
import io.github.fireres.gui.controller.excess.pressure.ExcessPressure;
import io.github.fireres.gui.preset.Preset;
import io.github.fireres.gui.preset.PresetApplier;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory.DoubleSpinnerValueFactory;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExcessPressurePresetApplier implements PresetApplier<ExcessPressure> {

    private final Cloner cloner;
    private final ExcessPressureBasePressureProperties basePressureProperties;
    private final ExcessPressureDispersionCoefficientProperties dispersionCoefficientProperties;
    private final ExcessPressureDeltaProperties deltaProperties;

    @Override
    public void apply(ExcessPressure excessPressure, Preset preset) {
        val sampleProperties = excessPressure.getSample().getSampleProperties();
        val presetProperties = cloner.deepClone(preset.getProperties(ExcessPressureProperties.class));

        sampleProperties.putReportProperties(presetProperties);

        val excessPressureParams = excessPressure.getExcessPressureParams();

        setBasePressure(excessPressureParams.getBasePressure(), presetProperties);
        setDispersionCoefficient(excessPressureParams.getDispersionCoefficient(), presetProperties);
        setDelta(excessPressureParams.getDelta(), presetProperties);
    }

    private void setBasePressure(Spinner<Double> basePressure, ExcessPressureProperties properties) {
        basePressure.setValueFactory(new DoubleSpinnerValueFactory(
                basePressureProperties.getMinValue(),
                basePressureProperties.getMaxValue(),
                properties.getBasePressure()));
    }

    private void setDispersionCoefficient(Spinner<Double> dispersionCoefficient,
                                          ExcessPressureProperties properties) {

        dispersionCoefficient.setValueFactory(new DoubleSpinnerValueFactory(
                dispersionCoefficientProperties.getMinValue(),
                dispersionCoefficientProperties.getMaxValue(),
                properties.getDispersionCoefficient(),
                dispersionCoefficientProperties.getIncrement()));
    }

    private void setDelta(Spinner<Double> delta, ExcessPressureProperties properties) {
        delta.setValueFactory(new DoubleSpinnerValueFactory(
                deltaProperties.getMinValue(),
                deltaProperties.getMaxValue(),
                properties.getDelta()));
    }

}

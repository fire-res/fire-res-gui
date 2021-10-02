package io.github.fireres.gui.configurer.report;

import com.rits.cloning.Cloner;
import io.github.fireres.excess.pressure.properties.ExcessPressureProperties;
import io.github.fireres.gui.controller.excess.pressure.ExcessPressure;
import io.github.fireres.gui.preset.Preset;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory.DoubleSpinnerValueFactory;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExcessPressureParametersConfigurer extends AbstractReportParametersConfigurer<ExcessPressure> {

    private static final Double BASE_PRESSURE_MIN = 0d;
    private static final Double BASE_PRESSURE_MAX = 1000d;

    private static final Double DISPERSION_COEFFICIENT_MIN = 0d;
    private static final Double DISPERSION_COEFFICIENT_MAX = 1d;
    private static final Double DISPERSION_COEFFICIENT_INCREMENT = 0.01;

    private static final Double DELTA_MIN = 0d;
    private static final Double DELTA_MAX = 1000d;

    private final Cloner cloner;

    @Override
    public void config(ExcessPressure excessPressure, Preset preset) {
        val sampleProperties = excessPressure.getSample().getSampleProperties();
        val presetProperties = cloner.deepClone(preset.getProperties(ExcessPressureProperties.class));

        sampleProperties.putReportProperties(presetProperties);

        val excessPressureParams = excessPressure.getExcessPressureParams();

        resetBasePressure(excessPressureParams.getBasePressure(), presetProperties);
        resetDispersionCoefficient(excessPressureParams.getDispersionCoefficient(), presetProperties);
        resetDelta(excessPressureParams.getDelta(), presetProperties);
    }

    private void resetBasePressure(Spinner<Double> basePressure, ExcessPressureProperties properties) {
        basePressure.setValueFactory(new DoubleSpinnerValueFactory(
                BASE_PRESSURE_MIN,
                BASE_PRESSURE_MAX,
                properties.getBasePressure()));
    }

    private void resetDispersionCoefficient(Spinner<Double> dispersionCoefficient,
                                            ExcessPressureProperties properties) {

        dispersionCoefficient.setValueFactory(new DoubleSpinnerValueFactory(
                DISPERSION_COEFFICIENT_MIN,
                DISPERSION_COEFFICIENT_MAX,
                properties.getDispersionCoefficient(),
                DISPERSION_COEFFICIENT_INCREMENT));
    }

    private void resetDelta(Spinner<Double> delta, ExcessPressureProperties properties) {
        delta.setValueFactory(new DoubleSpinnerValueFactory(
                DELTA_MIN,
                DELTA_MAX,
                properties.getDelta()));
    }

}

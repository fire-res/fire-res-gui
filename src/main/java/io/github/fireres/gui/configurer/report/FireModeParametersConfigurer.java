package io.github.fireres.gui.configurer.report;

import com.rits.cloning.Cloner;
import io.github.fireres.firemode.model.FireModeType;
import io.github.fireres.firemode.properties.FireModeProperties;
import io.github.fireres.gui.controller.fire.mode.FireMode;
import io.github.fireres.gui.preset.Preset;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FireModeParametersConfigurer extends AbstractReportParametersConfigurer<FireMode> {

    private static final Integer THERMOCOUPLE_COUNT_MIN = 1;
    private static final Integer THERMOCOUPLE_COUNT_MAX = 100;

    private static final Integer TEMPERATURE_MAINTAINING_MIN = 0;
    private static final Integer TEMPERATURE_MAINTAINING_MAX = Integer.MAX_VALUE;

    private final Cloner cloner;

    @Override
    public void config(FireMode fireMode, Preset preset) {
        val sampleProperties = fireMode.getSample().getSampleProperties();
        val presetProperties = cloner.deepClone(preset.getProperties(FireModeProperties.class));

        sampleProperties.putReportProperties(presetProperties);

        val fireModeParams = fireMode.getFireModeParams();

        resetThermocoupleCount(fireModeParams.getThermocouples(), presetProperties);
        resetFireModeType(fireModeParams.getFireModeType(), presetProperties);
        resetStandardTemperatureMaintaining(fireModeParams.getTemperatureMaintaining(), presetProperties);
        resetShowBounds(fireModeParams.getShowBounds(), presetProperties);
        resetShowMeanTemperature(fireModeParams.getShowMeanTemperature(), presetProperties);

        resetFunctionParameters(fireMode.getFunctionParams(), presetProperties.getFunctionForm());
    }

    private void resetThermocoupleCount(Spinner<Integer> thermocoupleSpinner, FireModeProperties properties) {
        thermocoupleSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(
                THERMOCOUPLE_COUNT_MIN,
                THERMOCOUPLE_COUNT_MAX,
                properties.getThermocoupleCount()));
    }

    private void resetFireModeType(ChoiceBox<FireModeType> fireModeType, FireModeProperties properties) {
        fireModeType.getSelectionModel().select(properties.getFireModeType());
    }

    private void resetStandardTemperatureMaintaining(Spinner<Integer> standardTemperatureMaintaining,
                                                     FireModeProperties properties) {

        standardTemperatureMaintaining.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(
                TEMPERATURE_MAINTAINING_MIN,
                TEMPERATURE_MAINTAINING_MAX,
                properties.getTemperaturesMaintaining() != null
                        ? properties.getTemperaturesMaintaining()
                        : 0));
    }

    private void resetShowBounds(CheckBox showBounds, FireModeProperties properties) {
        showBounds.setSelected(properties.getShowBounds());
    }

    private void resetShowMeanTemperature(CheckBox showMeanTemperature, FireModeProperties properties) {
        showMeanTemperature.setSelected(properties.getShowMeanTemperature());
    }
}

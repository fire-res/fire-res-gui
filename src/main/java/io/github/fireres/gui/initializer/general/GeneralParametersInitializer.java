package io.github.fireres.gui.initializer.general;

import io.github.fireres.core.properties.GenerationProperties;
import io.github.fireres.gui.config.properties.general.EnvTemperatureProperties;
import io.github.fireres.gui.config.properties.general.TimeProperties;
import io.github.fireres.gui.controller.common.GeneralParams;
import io.github.fireres.gui.initializer.Initializer;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GeneralParametersInitializer implements Initializer<GeneralParams> {
    
    private final EnvTemperatureProperties envTemperatureProperties;
    private final TimeProperties timeProperties;
    private final GenerationProperties generationProperties;

    @Override
    public void initialize(GeneralParams generalParams) {
        initializeTime(generalParams.getTime());
        initializeEnvironmentTemperature(generalParams.getEnvironmentTemperature());
    }

    private void initializeTime(Spinner<Integer> time) {
        generationProperties.getGeneral().setTime(timeProperties.getDefaultValue() + 1);

        time.setValueFactory(new IntegerSpinnerValueFactory(
                timeProperties.getMinValue(),
                timeProperties.getMaxValue(),
                timeProperties.getDefaultValue()));
    }

    private void initializeEnvironmentTemperature(Spinner<Integer> environmentTemperature) {
        generationProperties.getGeneral()
                .setEnvironmentTemperature(envTemperatureProperties.getDefaultValue());

        environmentTemperature.setValueFactory(new IntegerSpinnerValueFactory(
                envTemperatureProperties.getMinValue(),
                envTemperatureProperties.getMaxValue(),
                envTemperatureProperties.getDefaultValue()));
    }

}

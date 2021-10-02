package io.github.fireres.gui.configurer;

import io.github.fireres.core.properties.GenerationProperties;
import io.github.fireres.gui.controller.common.GeneralParams;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GeneralParametersConfigurer implements Configurer<GeneralParams> {
    
    private static final Integer DEFAULT_ENV_TEMPERATURE = 21;
    private static final Integer DEFAULT_ENV_TEMPERATURE_MIN = 0;
    private static final Integer DEFAULT_ENV_TEMPERATURE_MAX = 100;

    private static final Integer DEFAULT_TIME = 50;
    private static final Integer DEFAULT_TIME_MIN = 1;
    private static final Integer DEFAULT_TIME_MAX = 1000;

    private final GenerationProperties generationProperties;

    @Override
    public void config(GeneralParams generalParams) {
        resetTime(generalParams.getTime());
        resetEnvironmentTemperature(generalParams.getEnvironmentTemperature());
    }

    private void resetEnvironmentTemperature(Spinner<Integer> environmentTemperature) {
        generationProperties.getGeneral().setEnvironmentTemperature(DEFAULT_ENV_TEMPERATURE);

        environmentTemperature.setValueFactory(new IntegerSpinnerValueFactory(
                DEFAULT_ENV_TEMPERATURE_MIN,
                DEFAULT_ENV_TEMPERATURE_MAX,
                DEFAULT_ENV_TEMPERATURE));
    }

    private void resetTime(Spinner<Integer> time) {
        generationProperties.getGeneral().setTime(DEFAULT_TIME + 1);

        time.setValueFactory(new IntegerSpinnerValueFactory(
                DEFAULT_TIME_MIN,
                DEFAULT_TIME_MAX,
                DEFAULT_TIME));
    }

}

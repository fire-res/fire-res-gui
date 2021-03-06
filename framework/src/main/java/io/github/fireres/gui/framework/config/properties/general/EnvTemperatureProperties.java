package io.github.fireres.gui.framework.config.properties.general;

import io.github.fireres.gui.framework.config.properties.IntegerSpinnerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "fire-res.general-params.env-temperature")
public class EnvTemperatureProperties extends IntegerSpinnerProperties {
}

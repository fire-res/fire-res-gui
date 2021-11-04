package io.github.fireres.gui.firemode.config.properties;

import io.github.fireres.gui.framework.config.properties.IntegerSpinnerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "fire-res.fire-mode.thermocouple-count")
public class FireModeThermocoupleCountProperties extends IntegerSpinnerProperties {
}

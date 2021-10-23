package io.github.fireres.gui.config.properties.firemode;

import io.github.fireres.gui.config.properties.IntegerSpinnerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "fire-res.fire-mode.temperature-maintaining")
public class FireModeTemperatureMaintainingProperties extends IntegerSpinnerProperties {
}

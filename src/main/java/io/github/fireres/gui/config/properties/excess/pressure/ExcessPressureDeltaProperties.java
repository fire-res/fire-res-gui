package io.github.fireres.gui.config.properties.excess.pressure;

import io.github.fireres.gui.config.properties.DoubleSpinnerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "fire-res.excess-pressure.delta")
public class ExcessPressureDeltaProperties extends DoubleSpinnerProperties {
}

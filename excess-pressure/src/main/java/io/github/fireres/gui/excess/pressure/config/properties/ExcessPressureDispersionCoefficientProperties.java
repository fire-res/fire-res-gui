package io.github.fireres.gui.excess.pressure.config.properties;

import io.github.fireres.gui.framework.config.properties.DoubleSpinnerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "fire-res.excess-pressure.dispersion-coefficient")
public class ExcessPressureDispersionCoefficientProperties extends DoubleSpinnerProperties {
}

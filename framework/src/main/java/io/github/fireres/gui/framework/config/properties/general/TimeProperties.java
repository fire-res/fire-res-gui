package io.github.fireres.gui.framework.config.properties.general;

import io.github.fireres.gui.framework.config.properties.IntegerSpinnerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "fire-res.general-params.time")
public class TimeProperties extends IntegerSpinnerProperties {
}

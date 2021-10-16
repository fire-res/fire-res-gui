package io.github.fireres.gui.config.properties.heatflow;

import io.github.fireres.gui.config.properties.IntegerSpinnerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "fire-res.heat-flow.sensors")
public class HeatFlowSensorsProperties extends IntegerSpinnerProperties {
}

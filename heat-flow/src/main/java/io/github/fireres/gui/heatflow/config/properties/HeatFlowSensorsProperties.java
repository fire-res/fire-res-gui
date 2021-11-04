package io.github.fireres.gui.heatflow.config.properties;

import io.github.fireres.gui.framework.config.properties.IntegerSpinnerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "fire-res.heat-flow.sensors")
public class HeatFlowSensorsProperties extends IntegerSpinnerProperties {
}

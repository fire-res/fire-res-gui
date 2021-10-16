package io.github.fireres.gui.config.properties.heatflow;

import io.github.fireres.gui.config.properties.DoubleSpinnerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "fire-res.heat-flow.bound")
public class HeatFlowBoundProperties extends DoubleSpinnerProperties {
}

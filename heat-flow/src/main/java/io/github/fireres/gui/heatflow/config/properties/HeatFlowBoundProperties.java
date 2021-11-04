package io.github.fireres.gui.heatflow.config.properties;

import io.github.fireres.gui.framework.config.properties.DoubleSpinnerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "fire-res.heat-flow.bound")
public class HeatFlowBoundProperties extends DoubleSpinnerProperties {
}

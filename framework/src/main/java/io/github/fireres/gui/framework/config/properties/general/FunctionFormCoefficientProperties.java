package io.github.fireres.gui.framework.config.properties.general;

import io.github.fireres.gui.framework.config.properties.DoubleSpinnerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "fire-res.function-form.coefficients")
public class FunctionFormCoefficientProperties extends DoubleSpinnerProperties {

}

package io.github.fireres.gui.framework.config.properties;

import lombok.Data;

@Data
public abstract class IntegerSpinnerProperties {

    private Integer defaultValue;
    private Integer minValue;
    private Integer maxValue;

}

package io.github.fireres.gui.config.properties;

import lombok.Data;

@Data
public abstract class IntegerSpinnerProperties {

    private Integer defaultValue;
    private Integer minValue;
    private Integer maxValue;

}

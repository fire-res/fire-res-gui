package io.github.fireres.gui.framework.config.properties;

import lombok.Data;

@Data
public abstract class DoubleSpinnerProperties {

    private Double defaultValue;
    private Double minValue;
    private Double maxValue;
    private Double increment;

}

package io.github.fireres.gui.config.properties;

import lombok.Data;

@Data
public abstract class DoubleSpinnerProperties {

    private Double defaultValue;
    private Double minValue;
    private Double maxValue;
    private Double increment;

}

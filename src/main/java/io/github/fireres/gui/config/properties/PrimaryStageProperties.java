package io.github.fireres.gui.config.properties;

import lombok.Data;

@Data
public class PrimaryStageProperties {

    private String title;
    private Integer minWidth;
    private Integer minHeight;
    private Boolean resizable;
    private Boolean maximized;

}

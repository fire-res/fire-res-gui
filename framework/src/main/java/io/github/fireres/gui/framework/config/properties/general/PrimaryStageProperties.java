package io.github.fireres.gui.framework.config.properties.general;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "fire-res.primary-stage")
public class PrimaryStageProperties {

    private String title;
    private Integer minWidth;
    private Integer minHeight;
    private Boolean resizable;
    private Boolean maximized;

}

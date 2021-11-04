package io.github.fireres.gui.framework.config.properties.general;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "fire-res.presets")
public class PresetsProperties {

    private String defaultPresetsPath;
    private String customPresetsPath;

}

package io.github.fireres.gui.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "fire-res")
public class ApplicationProperties {

    private PresetsProperties presets;
    private Integer threadsCount;
    private PrimaryStageProperties primaryStage;

}

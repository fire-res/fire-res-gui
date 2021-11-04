package io.github.fireres.gui.framework.config.properties.general;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "fire-res.general")
public class GeneralProperties {

    private String userGuideLink;
    private String repositoryLink;
    private String version;

}

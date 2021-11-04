package io.github.fireres.gui.framework.config;

import io.github.fireres.core.properties.GeneralProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GeneralPropertiesConfig {

    @Bean
    public GeneralProperties generalProperties() {
        return new GeneralProperties();
    }

}

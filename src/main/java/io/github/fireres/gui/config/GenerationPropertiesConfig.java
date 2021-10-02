package io.github.fireres.gui.config;

import io.github.fireres.core.properties.GenerationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GenerationPropertiesConfig {

    @Bean
    public GenerationProperties generationProperties() {
        return new GenerationProperties();
    }

}

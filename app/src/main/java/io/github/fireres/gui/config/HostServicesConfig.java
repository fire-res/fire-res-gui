package io.github.fireres.gui.config;

import io.github.fireres.gui.FireResJavaFxApplication;
import io.github.fireres.gui.framework.service.HostServicesProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HostServicesConfig {

    @Bean
    public HostServicesProvider hostServicesProvider() {
        return () -> FireResJavaFxApplication.hostServices;
    }

}

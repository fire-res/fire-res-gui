package io.github.fireres.gui.config;

import io.github.fireres.gui.config.properties.ApplicationProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
@RequiredArgsConstructor
public class ExecutorServiceConfig {

    private final ApplicationProperties applicationProperties;

    @Bean
    public ExecutorService executorService() {
        return Executors.newFixedThreadPool(applicationProperties.getThreadsCount());
    }

}

package io.github.fireres.gui.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
@RequiredArgsConstructor
public class ExecutorServiceConfig {

    @Bean
    public ExecutorService executorService(@Value("${fire-res.threads-count}") Integer threadsCount) {
        return Executors.newFixedThreadPool(threadsCount);
    }

}

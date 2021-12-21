package io.github.fireres.gui.framework.config;

import io.github.fireres.gui.framework.controller.ExceptionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import javax.annotation.PostConstruct;

@Configuration
@RequiredArgsConstructor
public class GlobalExceptionHandlerConfig {

    private final ExceptionHandler exceptionHandler;

    @PostConstruct
    public void postConstruct() {
        Thread.setDefaultUncaughtExceptionHandler(exceptionHandler);
    }

}

package io.github.fireres.gui.config;

import io.github.fireres.gui.annotation.processor.AnnotationProcessor;
import io.github.fireres.gui.annotation.processor.InitializeAnnotationProcessor;
import io.github.fireres.gui.annotation.processor.GenerateReportAnnotationProcessor;
import io.github.fireres.gui.annotation.processor.ModalWindowAnnotationProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class AnnotationProcessorsConfig {

    @Bean
    public List<AnnotationProcessor> annotationProcessors(
            InitializeAnnotationProcessor initializeAnnotationProcessor,
            GenerateReportAnnotationProcessor generateReportAnnotationProcessor,
            ModalWindowAnnotationProcessor modalWindowAnnotationProcessor
    ) {
        return List.of(
                initializeAnnotationProcessor,
                generateReportAnnotationProcessor,
                modalWindowAnnotationProcessor
        );
    }

}

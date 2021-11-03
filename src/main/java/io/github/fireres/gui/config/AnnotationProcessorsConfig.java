package io.github.fireres.gui.config;

import io.github.fireres.gui.annotation.processor.AnnotationProcessor;
import io.github.fireres.gui.annotation.processor.ColumnPropertyAnnotationProcessor;
import io.github.fireres.gui.annotation.processor.ContextMenuAnnotationProcessor;
import io.github.fireres.gui.annotation.processor.InitializeAnnotationProcessor;
import io.github.fireres.gui.annotation.processor.ModalWindowAnnotationProcessor;
import io.github.fireres.gui.annotation.processor.TableContextMenuAnnotationProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class AnnotationProcessorsConfig {

    @Bean
    public List<AnnotationProcessor> annotationProcessors(
            InitializeAnnotationProcessor initializeAnnotationProcessor,
            ModalWindowAnnotationProcessor modalWindowAnnotationProcessor,
            ContextMenuAnnotationProcessor contextMenuAnnotationProcessor,
            TableContextMenuAnnotationProcessor tableContextMenuAnnotationProcessor,
            ColumnPropertyAnnotationProcessor columnPropertyAnnotationProcessor
    ) {
        return List.of(
                initializeAnnotationProcessor,
                modalWindowAnnotationProcessor,
                contextMenuAnnotationProcessor,
                tableContextMenuAnnotationProcessor,
                columnPropertyAnnotationProcessor
        );
    }

}

package io.github.fireres.gui.annotation.processor;

import io.github.fireres.gui.annotation.Initialize;
import io.github.fireres.gui.controller.ExtendedComponent;
import io.github.fireres.gui.initializer.Initializer;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class InitializeAnnotationProcessor implements AnnotationProcessor {

    private final List<Initializer<?>> initializers;

    @SuppressWarnings("unchecked")
    @Override
    public void process(ExtendedComponent<?> component) {
        if (component.getClass().isAnnotationPresent(Initialize.class)) {
            val annotation = component.getClass().getAnnotation(Initialize.class);
            val initializer = lookupConfigurer(annotation.value());

            initializer.initialize(component);
        }
    }

    @SuppressWarnings("rawtypes")
    private Initializer lookupConfigurer(Class<? extends Initializer<?>> configurerClass) {
        return initializers.stream()
                .filter(c -> c.getClass().equals(configurerClass))
                .findFirst()
                .orElseThrow();
    }
}

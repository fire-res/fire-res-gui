package io.github.fireres.gui.annotation.processor;

import io.github.fireres.gui.controller.ExtendedComponent;

public interface AnnotationProcessor {

    void process(ExtendedComponent<?> component);

}

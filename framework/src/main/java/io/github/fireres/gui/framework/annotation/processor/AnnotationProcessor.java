package io.github.fireres.gui.framework.annotation.processor;

import io.github.fireres.gui.framework.controller.ExtendedComponent;

public interface AnnotationProcessor {

    void process(ExtendedComponent<?> component);

}

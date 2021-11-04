package io.github.fireres.gui.framework.annotation;

import javafx.stage.Modality;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ModalWindow {

    String title();

    boolean resizable() default false;

    boolean isAlwaysOnTop() default false;

    Modality modality() default Modality.APPLICATION_MODAL;

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface Window {
    }

}

package io.github.fireres.gui.framework.annotation;

import io.github.fireres.gui.framework.initializer.Initializer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Initialize {

    Class<? extends Initializer<?>> value();

}

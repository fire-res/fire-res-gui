package io.github.fireres.gui.framework.annotation.processor;

import io.github.fireres.gui.framework.controller.ExtendedComponent;
import io.github.fireres.gui.framework.annotation.ContextMenu;
import lombok.SneakyThrows;
import lombok.val;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

import static io.github.fireres.gui.framework.util.ContextMenuUtils.buildContextMenu;

@Component
public class ContextMenuAnnotationProcessor implements AnnotationProcessor {

    @Override
    public void process(ExtendedComponent<?> component) {
        if (component.getClass().isAnnotationPresent(ContextMenu.class)) {
            processClassAnnotation(component);
        }

        val fields = component.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(ContextMenu.class)) {
                processFieldAnnotation(field, component);
            }
        }
    }

    private void processClassAnnotation(ExtendedComponent<?> component) {
        val annotation = component.getClass().getAnnotation(ContextMenu.class);

        val contextMenu = buildContextMenu(annotation, component);

        injectContextMenu(component.getComponent(), contextMenu);
    }

    @SneakyThrows
    private void processFieldAnnotation(Field field, ExtendedComponent<?> component) {
        val annotation = field.getAnnotation(ContextMenu.class);

        val contextMenu = buildContextMenu(annotation, component);

        field.setAccessible(true);
        val fieldValue = field.get(component);

        injectContextMenu(fieldValue, contextMenu);
    }

    @SneakyThrows
    private void injectContextMenu(Object component, javafx.scene.control.ContextMenu contextMenu) {
        val setter = component.getClass().getMethod("setContextMenu", javafx.scene.control.ContextMenu.class);

        setter.invoke(component, contextMenu);
    }

}

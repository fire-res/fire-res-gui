package io.github.fireres.gui.framework.util;

import io.github.fireres.gui.framework.annotation.ContextMenu;
import io.github.fireres.gui.framework.controller.ExtendedComponent;
import javafx.scene.control.MenuItem;
import lombok.experimental.UtilityClass;
import lombok.val;

import java.lang.reflect.Method;

@UtilityClass
public class ContextMenuUtils {

    public static javafx.scene.control.ContextMenu buildContextMenu(ContextMenu annotation,
                                                                    ExtendedComponent<?> component,
                                                                    Object... invocationParameters) {

        val items = annotation.value();
        val contextMenu = new javafx.scene.control.ContextMenu();

        for (ContextMenu.Item annotationItem : items) {
            val item = new MenuItem(annotationItem.text());
            val handler = findHandler(component, annotationItem.handler(), invocationParameters);

            handler.setAccessible(true);

            item.setOnAction(event -> {
                try {
                    handler.invoke(component, invocationParameters);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });

            contextMenu.getItems().add(item);
        }

        return contextMenu;
    }

    private Method findHandler(ExtendedComponent<?> component, String handlerName, Object... invocationParameters) {
        val methods = component.getClass().getDeclaredMethods();

        for (Method method : methods) {
            if (method.getName().equals(handlerName)) {
                validateMethod(method, invocationParameters);
                return method;
            }
        }

        throw new IllegalArgumentException("Method with name "
                + handlerName + " not found in " + component.getClass().getSimpleName());
    }

    private void validateMethod(Method method, Object[] invocationParameters) {
        if (!method.isAnnotationPresent(ContextMenu.Handler.class)) {
            throw new IllegalStateException("Method " + method.getName()
                    + " should be annotated with " + ContextMenu.Handler.class.getSimpleName());
        }

        if (method.getParameterCount() != invocationParameters.length) {
            throw new IllegalStateException("Method " + method.getName()
                    + " should have " + invocationParameters.length + " parameters");
        }

        val parametersTypes = method.getParameterTypes();
        for (int i = 0; i < parametersTypes.length; i++) {
            val expectedType = parametersTypes[i];
            val actualType = invocationParameters[i].getClass();

            if (!expectedType.equals(actualType)) {
                throw new IllegalStateException("Method " + method.getName()
                        + " should have parameter with type " + expectedType + " on index " + i);
            }
        }
    }

}

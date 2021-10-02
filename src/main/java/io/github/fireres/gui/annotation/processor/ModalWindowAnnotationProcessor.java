package io.github.fireres.gui.annotation.processor;

import io.github.fireres.gui.annotation.ModalWindow;
import io.github.fireres.gui.controller.ExtendedComponent;
import io.github.fireres.gui.model.Logos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
@RequiredArgsConstructor
public class ModalWindowAnnotationProcessor implements AnnotationProcessor {

    private final Logos logos;

    @Override
    public void process(ExtendedComponent<?> component) {
        if (component.getClass().isAnnotationPresent(ModalWindow.class)) {
            val modalWindowAnnotation = component.getClass().getAnnotation(ModalWindow.class);

            val window = new Stage();

            window.setScene(new Scene((Parent) component.getComponent()));
            window.setTitle(modalWindowAnnotation.title());
            window.setResizable(modalWindowAnnotation.resizable());
            window.initModality(modalWindowAnnotation.modality());
            window.getIcons().add(logos.getLogo512());
            window.setAlwaysOnTop(modalWindowAnnotation.isAlwaysOnTop());

            setWindowField(component, window);
        }
    }

    @SneakyThrows
    private void setWindowField(Object o, Stage window) {
        for (Field field : o.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(ModalWindow.Window.class)) {
                field.setAccessible(true);
                field.set(o, window);

                return;
            }
        }

        throw new IllegalStateException("Class " + o.getClass().getSimpleName() + " does not have window field");
    }

}

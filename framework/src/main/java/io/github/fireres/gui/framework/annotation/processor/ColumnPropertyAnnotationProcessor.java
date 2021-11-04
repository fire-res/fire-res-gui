package io.github.fireres.gui.framework.annotation.processor;

import io.github.fireres.gui.framework.controller.ExtendedComponent;
import io.github.fireres.gui.framework.annotation.ColumnProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.SneakyThrows;
import lombok.val;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
public class ColumnPropertyAnnotationProcessor implements AnnotationProcessor {

    @Override
    public void process(ExtendedComponent<?> component) {
        val fields = component.getClass().getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(ColumnProperty.class)) {
                validateField(field);
                setCellValueFactory(field, component);
            }
        }
    }

    private void validateField(Field field) {
        if (!field.getType().isAssignableFrom(TableColumn.class)) {
            throw new IllegalStateException("Field " + field.getName()
                    + " should be type of " + TableColumn.class.getSimpleName());
        }
    }

    @SneakyThrows
    private void setCellValueFactory(Field field, ExtendedComponent<?> component) {
        val property = field.getAnnotation(ColumnProperty.class).value();

        field.setAccessible(true);
        val column = ((TableColumn) field.get(component));

        column.setCellValueFactory(new PropertyValueFactory<>(property));
    }

}

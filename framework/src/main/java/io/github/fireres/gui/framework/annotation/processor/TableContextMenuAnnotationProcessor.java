package io.github.fireres.gui.framework.annotation.processor;

import io.github.fireres.gui.framework.annotation.TableContextMenu;
import io.github.fireres.gui.framework.controller.ExtendedComponent;
import io.github.fireres.core.model.Point;
import javafx.beans.binding.Bindings;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import lombok.SneakyThrows;
import lombok.val;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

import static io.github.fireres.gui.framework.util.ContextMenuUtils.buildContextMenu;

@Component
public class TableContextMenuAnnotationProcessor implements AnnotationProcessor {

    @Override
    public void process(ExtendedComponent<?> component) {
        val fields = component.getClass().getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(TableContextMenu.class)) {
                validateField(field);
                createTableContextMenu(field, component);
            }
        }
    }

    private void validateField(Field field) {
        if (!field.getType().isAssignableFrom(TableView.class)) {
            throw new IllegalStateException("Field " + field.getName()
                    + " should be type of " + TableView.class.getSimpleName());
        }
    }

    @SneakyThrows
    private void createTableContextMenu(Field field, ExtendedComponent<?> component) {
        val annotation = field.getAnnotation(TableContextMenu.class);

        field.setAccessible(true);
        val table = ((TableView) field.get(component));

        table.setContextMenu(buildContextMenu(annotation.table(), component));
        table.setRowFactory(
                t -> {
                    val row = new TableRow<Point<?>>();
                    val contextMenu = buildContextMenu(annotation.row(), component, row);

                    row.contextMenuProperty().bind(
                            Bindings.when(row.emptyProperty().not())
                                    .then(contextMenu)
                                    .otherwise((javafx.scene.control.ContextMenu) null));

                    return row;
                });
    }

}

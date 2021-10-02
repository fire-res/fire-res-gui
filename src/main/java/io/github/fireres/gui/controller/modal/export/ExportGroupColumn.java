package io.github.fireres.gui.controller.modal.export;

import io.github.fireres.gui.controller.AbstractComponent;
import io.github.fireres.gui.service.FxmlLoadService;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import lombok.RequiredArgsConstructor;
import lombok.val;
import io.github.fireres.gui.annotation.FxmlView;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@FxmlView("exportGroupColumn.fxml")
@RequiredArgsConstructor
@Component
@Scope(scopeName = SCOPE_PROTOTYPE)
public class ExportGroupColumn extends AbstractComponent<VBox> {

    private static final String GROUP_NAME_TEMPLATE = "Группа №%d";

    @FXML
    private VBox groupsVbox;

    private final FxmlLoadService fxmlLoadService;

    private final AtomicInteger groupCount = new AtomicInteger(0);

    public ExportGroup addGroup() {
        val group = fxmlLoadService.loadComponent(ExportGroup.class, this);

        group.setGroupName(String.format(GROUP_NAME_TEMPLATE, groupCount.incrementAndGet()));
        groupsVbox.getChildren().add(group.getComponent());

        return group;
    }

    public void removeGroup(ExportGroup group) {
        getChildren().removeIf(child -> child.equals(group));
        groupsVbox.getChildren().removeIf(child -> child.equals(group.getComponent()));

        val parent = (ExportModalWindow) getParent();

        group.getChildren(GroupedSample.class).forEach(groupedSample -> parent.addSample(groupedSample.getSample()));

        if (parent.getExportSampleColumn() != null) {
            parent.getExportSampleColumn().removeGroupSelector(group);
        }

        if (getChildren().isEmpty()) {
            parent.removeExportGroupColumn(this);
        }
    }
}

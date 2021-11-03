package io.github.fireres.gui.controller.modal.export;

import io.github.fireres.core.model.Sample;
import io.github.fireres.gui.annotation.FxmlView;
import io.github.fireres.gui.controller.AbstractComponent;
import io.github.fireres.gui.service.FxmlLoadService;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@FxmlView("exportSampleColumn.fxml")
@RequiredArgsConstructor
@Component
@Scope(scopeName = SCOPE_PROTOTYPE)
public class ExportSampleColumn extends AbstractComponent<VBox> {

    @FXML
    private VBox exportSamplesVbox;

    private final FxmlLoadService fxmlLoadService;

    public void addGroupSelector(ExportGroup group) {
        getChildren(ExportSample.class).forEach(exportSample -> exportSample.addGroupSelector(group));
    }

    public void removeGroupSelector(ExportGroup group) {
        getChildren(ExportSample.class).forEach(exportSample -> exportSample.removeGroupSelector(group));
    }

    public ExportSample addSample(Sample sample) {
        val exportSample = fxmlLoadService.loadComponent(ExportSample.class, this,
                component -> component.setSample(sample));

        exportSamplesVbox.getChildren().add(exportSample.getComponent());

        return exportSample;
    }

    public void removeSample(ExportSample exportSample) {
        getChildren().removeIf(child -> child.equals(exportSample));
        exportSamplesVbox.getChildren().removeIf(child -> child.equals(exportSample.getComponent()));

        if (getChildren().isEmpty()) {
            ((ExportModalWindow) getParent()).removeExportSampleColumn(this);
        }
    }

}

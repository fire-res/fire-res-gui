package io.github.fireres.gui.controller.modal.export;

import io.github.fireres.core.model.Sample;
import io.github.fireres.gui.controller.AbstractComponent;
import io.github.fireres.gui.service.FxmlLoadService;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import lombok.RequiredArgsConstructor;
import lombok.val;
import io.github.fireres.gui.annotation.FxmlView;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@FxmlView("exportGroup.fxml")
@RequiredArgsConstructor
@Component
@Scope(scopeName = SCOPE_PROTOTYPE)
public class ExportGroup extends AbstractComponent<TitledPane> {

    @FXML
    private TextField fileNameField;

    @FXML
    private VBox groupedSamplesVbox;

    private final FxmlLoadService fxmlLoadService;

    public void setGroupName(String groupName) {
        this.getComponent().setText(groupName);
        setFileName(groupName);
    }

    public String getGroupName() {
        return this.getComponent().getText();
    }

    public void setFileName(String fileName) {
        fileNameField.setText(fileName);
    }

    public String getFileName() {
        return fileNameField.getText();
    }

    public List<Sample> getSamples() {
        return getChildren(GroupedSample.class).stream()
                .map(GroupedSample::getSample)
                .collect(Collectors.toList());
    }

    public void addSample(Sample sample) {
        val groupedSample = fxmlLoadService.loadComponent(GroupedSample.class, this,
                component -> component.setSample(sample));

        groupedSamplesVbox.getChildren().add(groupedSample.getComponent());
        groupedSample.getComponent().requestFocus();
    }

    public void removeSample(GroupedSample groupedSample) {
        getChildren().removeIf(child -> child.equals(groupedSample));
        groupedSamplesVbox.getChildren().removeIf(child -> child.equals(groupedSample.getComponent()));
        ((ExportModalWindow) getParent().getParent()).addSample(groupedSample.getSample());
    }

    @FXML
    public void remove() {
        ((ExportGroupColumn) getParent()).removeGroup(this);
    }
}

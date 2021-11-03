package io.github.fireres.gui.controller.modal.export;

import io.github.fireres.core.model.Sample;
import io.github.fireres.gui.annotation.FxmlView;
import io.github.fireres.gui.controller.AbstractComponent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import lombok.Getter;
import lombok.Setter;
import lombok.val;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@Component
@Scope(scopeName = SCOPE_PROTOTYPE)
@FxmlView("exportSample.fxml")
public class ExportSample extends AbstractComponent<CheckBox> {

    @FXML
    private MenuButton groupSelector;

    @FXML
    private TitledPane titledPane;

    @FXML
    @Getter
    private TextField fileName;

    @Setter
    @Getter
    private Sample sample;

    @Override
    public void postConstruct() {
        titledPane.setText(sample.getSampleProperties().getName());
        fileName.setText(sample.getSampleProperties().getName());
    }

    @FXML
    public void changeSampleInclusion() {
        if (getComponent().isSelected()) {
            titledPane.setDisable(false);
        } else {
            titledPane.setDisable(true);
            titledPane.setExpanded(false);
        }
    }

    public void addGroupSelector(ExportGroup group) {
        val groupItem = new MenuItem(group.getGroupName());

        groupItem.setOnAction(event -> {
            group.addSample(sample);
            ((ExportSampleColumn) getParent()).removeSample(this);
        });

        groupSelector.getItems().add(groupItem);
    }

    public void removeGroupSelector(ExportGroup group) {
        groupSelector.getItems().removeIf(menuItem -> menuItem.getText().equals(group.getGroupName()));
    }
}

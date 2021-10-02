package io.github.fireres.gui.controller.modal.export;

import io.github.fireres.core.model.Sample;
import io.github.fireres.gui.controller.AbstractComponent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import lombok.Getter;
import lombok.Setter;
import io.github.fireres.gui.annotation.FxmlView;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@FxmlView("groupedSample.fxml")
@Component
@Scope(scopeName = SCOPE_PROTOTYPE)
public class GroupedSample extends AbstractComponent<HBox> {

    @FXML
    private Label label;

    @Setter
    @Getter
    private Sample sample;

    @Override
    public void postConstruct() {
        label.setText(sample.getSampleProperties().getName());
    }

    public void remove() {
        ((ExportGroup) getParent()).removeSample(this);
    }
}

package io.github.fireres.gui.framework.controller.modal.preset.management;

import io.github.fireres.gui.framework.annotation.FxmlView;
import io.github.fireres.gui.framework.annotation.ModalWindow;
import io.github.fireres.gui.framework.controller.AbstractComponent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@ModalWindow(title = "Переименование пресета")
@FxmlView("presetRenameModalWindow.fxml")
@RequiredArgsConstructor
@Component
@Scope(scopeName = SCOPE_PROTOTYPE)
public class PresetRenameModalWindow extends AbstractComponent<Pane> {

    @FXML
    @Getter
    private TextArea description;

    @FXML
    @Getter
    private TextField filename;

    @ModalWindow.Window
    @Getter
    private Stage window;

    @Override
    public void postConstruct() {
        val preset = ((PresetItem) getParent()).getPreset();

        filename.setText(preset.getFilename());
        description.setText(preset.getDescription());
    }

    @FXML
    public void renamePreset() {
    }

    @FXML
    public void closeWindow() {
        window.close();
    }
}

package io.github.fireres.gui.framework.controller.modal.preset.management;

import io.github.fireres.gui.framework.annotation.FxmlView;
import io.github.fireres.gui.framework.annotation.ModalWindow;
import io.github.fireres.gui.framework.controller.AbstractComponent;
import io.github.fireres.gui.framework.service.FxmlLoadService;
import io.github.fireres.gui.framework.service.PresetService;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@ModalWindow(title = "Управление пресетами")
@FxmlView("presetManagementModalWindow.fxml")
@RequiredArgsConstructor
@Component
@Scope(scopeName = SCOPE_PROTOTYPE)
public class PresetManagementModalWindow extends AbstractComponent<AnchorPane> {

    @Getter
    @FXML
    private VBox presetsVbox;

    @ModalWindow.Window
    @Getter
    private Stage window;

    private final PresetService presetService;
    private final FxmlLoadService fxmlLoadService;

    @Override
    public void postConstruct() {
        loadPresets();
    }

    public void reloadPresets() {
        removePresets();
        loadPresets();
    }

    private void removePresets() {
        removeChildren(PresetItem.class);
        presetsVbox.getChildren().clear();
    }

    private void loadPresets() {
        val availablePresets = presetService.getAvailablePresets();

        availablePresets.forEach(preset -> {
            val presetItem = fxmlLoadService.loadComponent(PresetItem.class, this);

            presetItem.getPresetDescription().setText(preset.getDescription());
            presetItem.setPreset(preset);

            this.getPresetsVbox().getChildren().add(presetItem.getComponent());
        });
    }

}

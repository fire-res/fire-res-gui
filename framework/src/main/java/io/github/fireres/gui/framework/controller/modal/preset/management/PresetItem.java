package io.github.fireres.gui.framework.controller.modal.preset.management;

import io.github.fireres.gui.framework.annotation.FxmlView;
import io.github.fireres.gui.framework.controller.AbstractComponent;
import io.github.fireres.gui.framework.preset.Preset;
import io.github.fireres.gui.framework.service.AlertService;
import io.github.fireres.gui.framework.service.FxmlLoadService;
import io.github.fireres.gui.framework.service.PresetService;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@FxmlView("presetItem.fxml")
@RequiredArgsConstructor
@Component
@Scope(scopeName = SCOPE_PROTOTYPE)
public class PresetItem extends AbstractComponent<HBox> {

    @FXML
    @Getter
    private Text presetDescription;

    @Setter
    @Getter
    private Preset preset;

    private final FxmlLoadService fxmlLoadService;
    private final AlertService alertService;
    private final PresetService presetService;

    @FXML
    public void handleRenamePresetPressed() {
        fxmlLoadService.loadComponent(PresetRenameModalWindow.class, this).getWindow().show();
    }

    @FXML
    public void handleDeletePresetPressed() {
        alertService.showConfirmation(
                "Вы действительно хотите удалить пересет \"" + preset.getDescription() + "\"?",
                this::deletePreset);
    }

    private void deletePreset() {
        presetService.deletePreset(preset);
        ((PresetManagementModalWindow) getParent()).reloadPresets();
    }

}

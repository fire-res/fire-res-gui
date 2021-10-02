package io.github.fireres.gui.controller.modal;

import io.github.fireres.gui.annotation.ModalWindow;
import io.github.fireres.gui.controller.AbstractComponent;
import io.github.fireres.gui.controller.PresetChanger;
import io.github.fireres.gui.controller.PresetContainer;
import io.github.fireres.gui.preset.Preset;
import io.github.fireres.gui.service.AlertService;
import io.github.fireres.gui.service.PresetService;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import io.github.fireres.gui.annotation.FxmlView;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@ModalWindow(title = "Изменение пресета")
@FxmlView("samplePresetChangeModalWindow.fxml")
@RequiredArgsConstructor
@Component
@Scope(scopeName = SCOPE_PROTOTYPE)
public class SamplePresetChangeModalWindow extends AbstractComponent<Pane> {

    @ModalWindow.Window
    @Getter
    private Stage window;

    @FXML
    public ChoiceBox<Preset> presets;

    private final AlertService alertService;
    private final PresetService presetService;

    @Override
    public void postConstruct() {
        presets.getSelectionModel().selectedItemProperty().addListener((observableValue, preset, t1) ->
                handleAnotherPresetChosen());

        presets.getItems().addAll(presetService.getAvailablePresets());

        choosePreset(((PresetContainer) getParent()).getPreset());
    }

    @FXML
    public void closeWindow() {
        window.close();
    }

    @FXML
    public void changePreset() {
        alertService.showConfirmation(
                "Внимание! При изменении пресета все параметры " +
                        "в образце будут сброшены, вы хотите продолжить?",
                this::handlePresetChangeConfirmed);
    }

    private void handlePresetChangeConfirmed() {
        ((PresetChanger) getParent()).changePreset(presets.getValue());
    }

    private void choosePreset(Preset preset) {
        presets.getSelectionModel().select(preset);
        presets.setTooltip(new Tooltip(preset.getDescription()));
    }

    private void handleAnotherPresetChosen() {
        choosePreset(presets.getValue());
    }
}

package io.github.fireres.gui.framework.controller.modal;

import io.github.fireres.gui.framework.annotation.FxmlView;
import io.github.fireres.gui.framework.annotation.ModalWindow;
import io.github.fireres.gui.framework.component.FireResChoiceBox;
import io.github.fireres.gui.framework.controller.AbstractComponent;
import io.github.fireres.gui.framework.controller.PresetContainer;
import io.github.fireres.gui.framework.controller.common.SampleTab;
import io.github.fireres.gui.framework.preset.Preset;
import io.github.fireres.gui.framework.service.AlertService;
import io.github.fireres.gui.framework.service.PresetService;
import javafx.fxml.FXML;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
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
    public FireResChoiceBox<Preset> presets;

    private final AlertService alertService;
    private final PresetService presetService;

    @Override
    public void postConstruct() {
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
        val sampleTab = (SampleTab) getParent();

        sampleTab.getSample().removeAllReports();
        sampleTab.changePreset(presets.getValue());
    }

    private void choosePreset(Preset preset) {
        presets.getSelectionModel().select(preset);
        presets.setTooltip(new Tooltip(preset.getDescription()));
    }

    @FXML
    public void handleAnotherPresetChosen() {
        choosePreset(presets.getValue());
    }
}

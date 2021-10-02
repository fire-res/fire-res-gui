package io.github.fireres.gui.controller.modal;

import com.rits.cloning.Cloner;
import io.github.fireres.core.model.Sample;
import io.github.fireres.gui.annotation.ModalWindow;
import io.github.fireres.gui.controller.AbstractComponent;
import io.github.fireres.gui.controller.SampleContainer;
import io.github.fireres.gui.controller.common.SampleTab;
import io.github.fireres.gui.preset.Preset;
import io.github.fireres.gui.service.AlertService;
import io.github.fireres.gui.service.PresetService;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import io.github.fireres.gui.annotation.FxmlView;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@Slf4j
@FxmlView("sampleSavePresetModalWindow.fxml")
@ModalWindow(title = "Сохранение пресета")
@RequiredArgsConstructor
@Component
@Scope(scopeName = SCOPE_PROTOTYPE)
public class SampleSavePresetModalWindow extends AbstractComponent<Pane> implements SampleContainer {

    @FXML
    private TextField descriptionTextField;

    @ModalWindow.Window
    @Getter
    private Stage window;

    private final AlertService alertService;
    private final PresetService presetService;
    private final Cloner cloner;

    @Override
    protected void initialize() {
        descriptionTextField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                savePresetSample();
            }
        });
    }

    @Override
    public void postConstruct() {
        val currentSampleName = getSample().getSampleProperties().getName();

        descriptionTextField.setText(currentSampleName);
    }

    public void closeWindow() {
        window.close();
    }

    @SneakyThrows
    public void savePresetSample() {
        log.info("Save sample button pressed");

        if (validateSampleNamePreset()) {
            presetService.savePreset(Preset.builder()
                    .applyingByDefault(false)
                    .description(descriptionTextField.getText())
                    .properties(cloner.deepClone(getSample().getSampleProperties().getPropertiesMap()))
                    .build());
            closeWindow();
        }
    }

    private boolean validateSampleNamePreset() {
        val presets = presetService.getAvailablePresets();
        val newSampleDescription = descriptionTextField.getText();

        if (newSampleDescription == null || newSampleDescription.isEmpty() || newSampleDescription.isBlank()) {
            alertService.showError("Описание пресета не может быть пустым");
            return false;
        }

        for (Preset preset : presets) {
            val description = preset.getDescription();

            if (newSampleDescription.equals(description)) {
                alertService.showError("Пресет с таким описанием уже существует");
                return false;
            }
        }

        return true;
    }

    @Override
    public Sample getSample() {
        return ((SampleTab) getParent()).getSample();
    }
}

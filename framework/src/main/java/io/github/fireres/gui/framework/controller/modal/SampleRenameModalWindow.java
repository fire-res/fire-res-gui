package io.github.fireres.gui.framework.controller.modal;

import io.github.fireres.core.model.Sample;
import io.github.fireres.gui.framework.annotation.FxmlView;
import io.github.fireres.gui.framework.annotation.ModalWindow;
import io.github.fireres.gui.framework.controller.AbstractComponent;
import io.github.fireres.gui.framework.controller.SampleContainer;
import io.github.fireres.gui.framework.controller.common.SampleTab;
import io.github.fireres.gui.framework.controller.common.SamplesTabPane;
import io.github.fireres.gui.framework.service.AlertService;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@Slf4j
@FxmlView("sampleRenameModalWindow.fxml")
@ModalWindow(title = "Переименование образца")
@RequiredArgsConstructor
@Component
@Scope(scopeName = SCOPE_PROTOTYPE)
public class SampleRenameModalWindow extends AbstractComponent<Pane> implements SampleContainer {

    @FXML
    private TextField renameTextField;

    @ModalWindow.Window
    @Getter
    private Stage window;

    private final AlertService alertService;

    @Override
    protected void initialize() {
        renameTextField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                renameSample();
            }
        });
    }

    @Override
    public void postConstruct() {
        val currentSampleName = getSample().getSampleProperties().getName();

        renameTextField.setText(currentSampleName);
    }

    public void closeWindow() {
        window.close();
    }

    public void renameSample() {
        log.info("Rename sample button pressed");

        if (validateSampleName()) {
            getSample().getSampleProperties().setName(renameTextField.getText());
            ((SampleTab) getParent()).getComponent().setText(renameTextField.getText());
            closeWindow();
        }
    }

    private boolean validateSampleName() {
        val samplesControllers = ((SamplesTabPane) getParent().getParent()).getSampleTabs();
        val newSampleName = renameTextField.getText();
        val currentSample = getSample();

        if (newSampleName == null || newSampleName.isEmpty() || newSampleName.isBlank()) {
            alertService.showError("Имя образца не может быть пустым");
            return false;
        }

        for (SampleTab sampleController : samplesControllers) {
            val sample = sampleController.getSample();

            if (currentSample.equals(sample)) {
                continue;
            }

            if (newSampleName.equals(sample.getSampleProperties().getName())) {
                alertService.showError("Образец с таким именем уже существует");
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

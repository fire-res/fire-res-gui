package io.github.fireres.gui.framework.controller.modal.preset.management;

import io.github.fireres.gui.framework.annotation.FxmlView;
import io.github.fireres.gui.framework.annotation.Initialize;
import io.github.fireres.gui.framework.annotation.ModalWindow;
import io.github.fireres.gui.framework.controller.AbstractComponent;
import io.github.fireres.gui.framework.initializer.modal.PresetManagementModalWindowInitializer;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@ModalWindow(title = "Управление пресетами")
@FxmlView("presetManagementModalWindow.fxml")
@RequiredArgsConstructor
@Component
@Scope(scopeName = SCOPE_PROTOTYPE)
@Initialize(PresetManagementModalWindowInitializer.class)
public class PresetManagementModalWindow extends AbstractComponent<AnchorPane> {

    @Getter
    @FXML
    private VBox presetsVbox;

    @ModalWindow.Window
    @Getter
    private Stage window;



}

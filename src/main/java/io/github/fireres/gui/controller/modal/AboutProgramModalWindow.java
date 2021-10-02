package io.github.fireres.gui.controller.modal;

import io.github.fireres.gui.annotation.ModalWindow;
import io.github.fireres.gui.controller.AbstractComponent;
import javafx.application.HostServices;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import io.github.fireres.gui.annotation.FxmlView;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@ModalWindow(title = "О программе")
@FxmlView("aboutProgramModalWindow.fxml")
@RequiredArgsConstructor
@Component
@Scope(scopeName = SCOPE_PROTOTYPE)
public class AboutProgramModalWindow extends AbstractComponent<AnchorPane> {

    @ModalWindow.Window
    @Getter
    private Stage window;

    @FXML
    private Hyperlink repositoryLink;

    private final HostServices hostServices;

    public void openGithubLink() {
        hostServices.showDocument(repositoryLink.getText());
    }

}


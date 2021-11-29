package io.github.fireres.gui.framework.controller.modal;

import io.github.fireres.gui.framework.annotation.FxmlView;
import io.github.fireres.gui.framework.annotation.ModalWindow;
import io.github.fireres.gui.framework.controller.AbstractComponent;
import io.github.fireres.gui.framework.model.Icons;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@ModalWindow(title = "Произошла ошибка")
@FxmlView("handledExceptionModalWindow.fxml")
@RequiredArgsConstructor
@Component
@Scope(scopeName = SCOPE_PROTOTYPE)
public class HandledExceptionModalWindow extends AbstractComponent<AnchorPane> {

    @FXML
    private Label windowTitle;

    @FXML
    private Label windowMessage;

    @FXML
    private Button windowButton;

    @FXML
    private ImageView imageView;

    @Setter
    private Throwable exception;

    @ModalWindow.Window
    @Getter
    private Stage window;

    private final Icons icons;

    @Override
    public void postConstruct() {
        windowTitle.setText("Внимание! Произошла ошибка");
        windowMessage.setText(exception.getClass().getCanonicalName() + ": " + exception.getMessage());
        windowButton.setText("Закрыть");

        imageView.setImage(icons.getWarning48());
    }

    @FXML
    public void closeWindow() {
        window.close();
    }
}


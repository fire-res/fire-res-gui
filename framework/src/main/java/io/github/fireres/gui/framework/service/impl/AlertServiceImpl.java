package io.github.fireres.gui.framework.service.impl;

import io.github.fireres.gui.framework.model.Logos;
import io.github.fireres.gui.framework.service.AlertService;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlertServiceImpl implements AlertService {

    public static final ButtonType OK = new ButtonType("ОК");
    public static final ButtonType CANCEL = new ButtonType("Отмена");

    private final Logos logos;

    @Override
    public void showError(String text) {
        val alert = new Alert(Alert.AlertType.ERROR, text, OK);

        alert.setTitle("Ошибка");
        alert.setHeaderText(null);
        alert.getDialogPane().getStylesheets().add("/io/github/fireres/gui/framework/css/style.css");
        alert.getDialogPane().getStyleClass().add("alert");
        ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add(logos.getLogo512());

        alert.showAndWait();
    }

    @Override
    public void showConfirmation(String text, Runnable onConfirm) {
        val alert = new Alert(Alert.AlertType.CONFIRMATION, text, OK, CANCEL);

        alert.setTitle("Подтверждение");
        alert.setHeaderText(null);
        alert.getDialogPane().getStylesheets().add("/io/github/fireres/gui/framework/css/style.css");
        alert.getDialogPane().getStyleClass().add("confirmation");
        ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add(logos.getLogo512());

        alert.getDialogPane().getScene().getWindow().setOnCloseRequest(event ->
                ((Stage) alert.getDialogPane().getScene().getWindow()).close());

        val result = alert.showAndWait();

        if (result.isPresent() && result.get() == OK) {
            onConfirm.run();
        }
    }

}

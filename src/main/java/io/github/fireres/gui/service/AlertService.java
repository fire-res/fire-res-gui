package io.github.fireres.gui.service;

public interface AlertService {

    void showError(String text);

    void showConfirmation(String text, Runnable onConfirm);

}

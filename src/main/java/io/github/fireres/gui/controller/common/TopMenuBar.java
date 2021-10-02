package io.github.fireres.gui.controller.common;

import io.github.fireres.gui.FireResJavaFxApplication;
import io.github.fireres.gui.controller.AbstractComponent;
import io.github.fireres.gui.controller.modal.AboutProgramModalWindow;
import io.github.fireres.gui.controller.modal.export.ExportModalWindow;
import io.github.fireres.gui.service.FxmlLoadService;
import javafx.fxml.FXML;
import javafx.scene.control.MenuBar;
import lombok.RequiredArgsConstructor;
import io.github.fireres.gui.annotation.FxmlView;
import org.springframework.stereotype.Component;

@FxmlView("topMenuBar.fxml")
@Component
@RequiredArgsConstructor
public class TopMenuBar extends AbstractComponent<MenuBar> {

    public static final String USER_GUIDE_LINK = "https://github.com/therealmonE/fire-res/wiki";

    private final FxmlLoadService fxmlLoadService;

    @FXML
    public void openExportModalWindow() {
        fxmlLoadService.loadComponent(ExportModalWindow.class, getParent()).getWindow().show();
    }

    @FXML
    public void openAboutProgramModalWindow() {
        fxmlLoadService.loadComponent(AboutProgramModalWindow.class, getParent()).getWindow().show();
    }

    @FXML
    public void openUserGuideLink() {
        FireResJavaFxApplication.hostServices.showDocument(USER_GUIDE_LINK);
    }

}

package io.github.fireres.gui.framework.controller.common;

import io.github.fireres.gui.framework.annotation.FxmlView;
import io.github.fireres.gui.framework.config.properties.general.GeneralProperties;
import io.github.fireres.gui.framework.controller.AbstractComponent;
import io.github.fireres.gui.framework.controller.modal.AboutProgramModalWindow;
import io.github.fireres.gui.framework.controller.modal.export.ExportModalWindow;
import io.github.fireres.gui.framework.controller.modal.preset.management.PresetManagementModalWindow;
import io.github.fireres.gui.framework.service.FxmlLoadService;
import io.github.fireres.gui.framework.service.HostServicesProvider;
import javafx.fxml.FXML;
import javafx.scene.control.MenuBar;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@FxmlView("topMenuBar.fxml")
@Component
@RequiredArgsConstructor
public class TopMenuBar extends AbstractComponent<MenuBar> {

    private final FxmlLoadService fxmlLoadService;
    private final HostServicesProvider hostServicesProvider;
    private final GeneralProperties generalProperties;

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
        hostServicesProvider.get().showDocument(generalProperties.getUserGuideLink());
    }

    @FXML
    public void openPresetManagementModalWindow() {
        fxmlLoadService.loadComponent(PresetManagementModalWindow.class, getParent()).getWindow().show();
    }
}

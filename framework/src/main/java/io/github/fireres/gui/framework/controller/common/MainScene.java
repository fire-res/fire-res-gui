package io.github.fireres.gui.framework.controller.common;

import io.github.fireres.gui.framework.annotation.FxmlView;
import io.github.fireres.gui.framework.controller.AbstractComponent;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@FxmlView("mainScene.fxml")
@RequiredArgsConstructor
@Component
public class MainScene extends AbstractComponent<BorderPane> {

    @FXML
    private TopMenuBar topMenuBarController;

    @FXML
    private GeneralParams generalParamsController;

    @FXML
    private SamplesTabPane samplesTabPaneController;

    public GeneralParams getGeneralParams() {
        return generalParamsController;
    }

    public SamplesTabPane getSamplesTabPane() {
        return samplesTabPaneController;
    }
}

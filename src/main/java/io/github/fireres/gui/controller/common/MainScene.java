package io.github.fireres.gui.controller.common;

import io.github.fireres.core.properties.GeneralProperties;
import io.github.fireres.core.properties.ReportType;
import io.github.fireres.gui.annotation.FxmlView;
import io.github.fireres.gui.controller.AbstractComponent;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;

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

    private final GeneralProperties generalProperties;

    @Override
    public void initialize() {
        generalProperties.getIncludedReports().addAll(Arrays.asList(ReportType.values()));
    }

    public GeneralParams getGeneralParams() {
        return generalParamsController;
    }

    public SamplesTabPane getSamplesTabPane() {
        return samplesTabPaneController;
    }
}

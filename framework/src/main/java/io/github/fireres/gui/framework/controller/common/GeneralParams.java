package io.github.fireres.gui.framework.controller.common;

import io.github.fireres.core.model.ReportType;
import io.github.fireres.core.properties.GeneralProperties;
import io.github.fireres.gui.framework.annotation.FxmlView;
import io.github.fireres.gui.framework.annotation.Initialize;
import io.github.fireres.gui.framework.controller.AbstractComponent;
import io.github.fireres.gui.framework.initializer.general.GeneralParametersInitializer;
import javafx.fxml.FXML;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.MenuButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.TitledPane;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Component;

@FxmlView("generalParams.fxml")
@RequiredArgsConstructor
@Component
@Initialize(GeneralParametersInitializer.class)
public class GeneralParams extends AbstractComponent<TitledPane> {

    @FXML
    @Getter
    private Spinner<Integer> time;

    @FXML
    @Getter
    private Spinner<Integer> environmentTemperature;

    @FXML
    @Getter
    private MenuButton includedReportsMenuButton;

    private final GeneralProperties generalProperties;

    @FXML
    public void handleTimeChanged() {
        generalProperties.setTime(time.getValue() + 1);

        findComponents(SampleTab.class).forEach(SampleTab::refresh);
    }

    @FXML
    public void handleEnvironmentTemperatureChanged() {
        generalProperties.setEnvironmentTemperature(environmentTemperature.getValue());

        findComponents(SampleTab.class).forEach(SampleTab::refresh);
    }

    public void changeReportInclusion(CheckMenuItem menuItem, ReportType reportType) {
        findComponents(SampleTab.class)
                .forEach(sampleTab -> {
                    val reportTab = sampleTab.getReportTabByType(reportType);

                    if (menuItem.isSelected()) {
                        reportTab.includeReport();
                    } else {
                        reportTab.excludeReport();
                    }
                });
    }
}

package io.github.fireres.gui.controller.common;

import io.github.fireres.core.properties.GeneralProperties;
import io.github.fireres.gui.annotation.FxmlView;
import io.github.fireres.gui.annotation.Initialize;
import io.github.fireres.gui.controller.AbstractComponent;
import io.github.fireres.gui.controller.ReportInclusionChanger;
import io.github.fireres.gui.initializer.general.GeneralParametersInitializer;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Spinner;
import javafx.scene.control.TitledPane;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Function;

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

    @FXML
    public void changeFireModeInclusion(Event event) {
        changeReportInclusion((CheckMenuItem) event.getTarget(), SampleTab::getFireMode);
    }

    @FXML
    public void changeExcessPressureInclusion(Event event) {
        changeReportInclusion((CheckMenuItem) event.getTarget(), SampleTab::getExcessPressure);
    }

    @FXML
    public void changeHeatFlowInclusion(Event event) {
        changeReportInclusion((CheckMenuItem) event.getTarget(), SampleTab::getHeatFlow);
    }

    @FXML
    public void changeUnheatedSurfaceInclusion(Event event) {
        changeReportInclusion((CheckMenuItem) event.getTarget(), SampleTab::getUnheatedSurface);
    }

    public void changeReportInclusion(CheckMenuItem menuItem, Function<SampleTab, ReportInclusionChanger> mapper) {
        findComponents(SampleTab.class)
                .forEach(sampleTabController -> {
                    if (menuItem.isSelected()) {
                        mapper.apply(sampleTabController).includeReport();
                    } else {
                        mapper.apply(sampleTabController).excludeReport();
                    }
                });
    }
}

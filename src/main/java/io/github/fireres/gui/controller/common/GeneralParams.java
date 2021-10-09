package io.github.fireres.gui.controller.common;

import io.github.fireres.core.properties.GenerationProperties;
import io.github.fireres.gui.configurer.GeneralParametersConfigurer;
import io.github.fireres.gui.controller.AbstractComponent;
import io.github.fireres.gui.controller.ReportInclusionChanger;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Spinner;
import javafx.scene.control.TitledPane;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import io.github.fireres.gui.annotation.FxmlView;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@FxmlView("generalParams.fxml")
@RequiredArgsConstructor
@Component
public class GeneralParams extends AbstractComponent<TitledPane> {

    @FXML
    @Getter
    private Spinner<Integer> time;

    @FXML
    @Getter
    private Spinner<Integer> environmentTemperature;

    private final GeneralParametersConfigurer generalParametersConfigurer;
    private final GenerationProperties generationProperties;

    @Override
    public void postConstruct() {
        generalParametersConfigurer.config(this);

        time.focusedProperty().addListener((observable, oldValue, newValue) ->
                handleTimeSpinnerLostFocus(newValue));

        environmentTemperature.focusedProperty().addListener((observable, oldValue, newValue) ->
                handleEnvironmentTemperatureSpinnerLostFocus(newValue));
    }

    private void handleTimeSpinnerLostFocus(Boolean focusValue) {
        handleSpinnerLostFocus(focusValue, time, () -> {
            generationProperties.getGeneral().setTime(time.getValue() + 1);

            findComponents(SampleTab.class).forEach(SampleTab::refresh);
        });
    }

    private void handleEnvironmentTemperatureSpinnerLostFocus(Boolean focusValue) {
        handleSpinnerLostFocus(focusValue, environmentTemperature, () -> {
            generationProperties.getGeneral().setEnvironmentTemperature(environmentTemperature.getValue());

            findComponents(SampleTab.class).forEach(SampleTab::refresh);
        });
    }

    public void changeFireModeInclusion(Event event) {
        changeReportInclusion((CheckMenuItem) event.getTarget(), SampleTab::getFireMode);
    }

    public void changeExcessPressureInclusion(Event event) {
        changeReportInclusion((CheckMenuItem) event.getTarget(), SampleTab::getExcessPressure);
    }

    public void changeHeatFlowInclusion(Event event) {
        changeReportInclusion((CheckMenuItem) event.getTarget(), SampleTab::getHeatFlow);
    }

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

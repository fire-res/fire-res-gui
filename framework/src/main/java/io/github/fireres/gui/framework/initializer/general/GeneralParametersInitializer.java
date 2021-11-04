package io.github.fireres.gui.framework.initializer.general;

import io.github.fireres.core.model.ReportType;
import io.github.fireres.gui.framework.config.properties.general.EnvTemperatureProperties;
import io.github.fireres.gui.framework.config.properties.general.TimeProperties;
import io.github.fireres.gui.framework.initializer.Initializer;
import io.github.fireres.core.properties.GeneralProperties;
import io.github.fireres.gui.framework.controller.common.GeneralParams;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GeneralParametersInitializer implements Initializer<GeneralParams> {

    private final EnvTemperatureProperties envTemperatureProperties;
    private final TimeProperties timeProperties;
    private final GeneralProperties generalProperties;
    private final List<ReportType> reportTypes;

    @Override
    public void initialize(GeneralParams generalParams) {
        initializeTime(generalParams.getTime());
        initializeEnvironmentTemperature(generalParams.getEnvironmentTemperature());
        initializeReportsMenuButton(generalParams);
    }

    private void initializeReportsMenuButton(GeneralParams generalParams) {
        generalProperties.getIncludedReports().addAll(reportTypes);

        reportTypes.forEach(reportType -> {
            val menuItem = new CheckMenuItem(reportType.getDescription());

            menuItem.setOnAction(event -> generalParams.changeReportInclusion(menuItem, reportType));
            menuItem.setSelected(true);

            generalParams.getIncludedReportsMenuButton().getItems().add(menuItem);
        });
    }

    private void initializeTime(Spinner<Integer> time) {
        generalProperties.setTime(timeProperties.getDefaultValue() + 1);

        time.setValueFactory(new IntegerSpinnerValueFactory(
                timeProperties.getMinValue(),
                timeProperties.getMaxValue(),
                timeProperties.getDefaultValue()));
    }

    private void initializeEnvironmentTemperature(Spinner<Integer> environmentTemperature) {
        generalProperties.setEnvironmentTemperature(envTemperatureProperties.getDefaultValue());

        environmentTemperature.setValueFactory(new IntegerSpinnerValueFactory(
                envTemperatureProperties.getMinValue(),
                envTemperatureProperties.getMaxValue(),
                envTemperatureProperties.getDefaultValue()));
    }

}

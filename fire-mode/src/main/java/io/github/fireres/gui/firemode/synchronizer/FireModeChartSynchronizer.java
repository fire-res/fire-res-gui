package io.github.fireres.gui.firemode.synchronizer;

import io.github.fireres.firemode.report.FireModeReport;
import io.github.fireres.gui.framework.model.ElementIds;
import io.github.fireres.gui.framework.synchronizer.ChartSynchronizer;
import io.github.fireres.gui.framework.util.ChartUtils;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import lombok.val;
import org.springframework.stereotype.Component;

import static io.github.fireres.firemode.utils.FireModeUtils.getMaintainedMaxAllowedTemperature;
import static io.github.fireres.firemode.utils.FireModeUtils.getMaintainedMinAllowedTemperature;
import static io.github.fireres.firemode.utils.FireModeUtils.getMaintainedStandardTemperature;
import static io.github.fireres.firemode.utils.FireModeUtils.getMaintainedThermocoupleMeanTemperature;
import static io.github.fireres.firemode.utils.FireModeUtils.getMaintainedThermocoupleTemperatures;

@Component
public class FireModeChartSynchronizer implements ChartSynchronizer<FireModeReport> {

    public static final String STANDARD_TEMPERATURE_TEXT = "Стандартный режим пожара";
    public static final String MAX_ALLOWED_TEMPERATURE_TEXT = "Максимальный допуск температуры";
    public static final String SHIFTED_MAX_ALLOWED_TEMPERATURE_TEXT = "Максимальный допуск температуры (со смещением)";
    public static final String MIN_ALLOWED_TEMPERATURE_TEXT = "Минимальный допуск температуры";
    public static final String SHIFTED_MIN_ALLOWED_TEMPERATURE_TEXT = "Минимальный допуск температуры (со смещением)";
    public static final String MEAN_TEMPERATURE_TEXT = "Средняя температура";
    public static final String THERMOCOUPLE_TEXT = "Термопара - ";

    @Override
    public void synchronize(LineChart<Number, Number> chart, FireModeReport report) {
        chart.getData().clear();

        addThermocoupleTemperatureLines(chart, report);

        if (report.getProperties().getShowMeanTemperature()) {
            addThermocoupleMeanTemperatureLine(chart, report);
        }

        if (report.getProperties().getShowBounds()) {
            addStandardTemperatureLine(chart, report);
            addMinAllowedTemperatureLine(chart, report);
            addShiftedMinAllowedTemperatureLine(chart, report);
            addMaxAllowedTemperatureLine(chart, report);
            addShiftedMaxAllowedTemperatureLine(chart, report);
        }

        ChartUtils.addLegendSymbolId(chart, STANDARD_TEMPERATURE_TEXT, ElementIds.FIREMODE_STANDARD_TEMPERATURE_LEGEND_SYMBOL);
        ChartUtils.addLegendSymbolId(chart, MEAN_TEMPERATURE_TEXT, ElementIds.DEFAULT_MEAN_LINE_LEGEND_SYMBOL);
    }

    private void addStandardTemperatureLine(LineChart<Number, Number> chart, FireModeReport report) {
        val series = new XYChart.Series<Number, Number>();

        series.setName(STANDARD_TEMPERATURE_TEXT);
        ChartUtils.addPointsToSeries(series, getMaintainedStandardTemperature(report));
        chart.getData().add(series);
        series.getNode().setId(ElementIds.FIREMODE_STANDARD_TEMPERATURE_LINE);
    }

    private void addMaxAllowedTemperatureLine(LineChart<Number, Number> chart, FireModeReport report) {
        val series = new XYChart.Series<Number, Number>();

        series.setName(MAX_ALLOWED_TEMPERATURE_TEXT);
        ChartUtils.addPointsToSeries(series, getMaintainedMaxAllowedTemperature(report));
        chart.getData().add(series);
        series.getNode().setId(ElementIds.FIREMODE_MAX_TEMPERATURE_LINE);
    }

    private void addShiftedMaxAllowedTemperatureLine(LineChart<Number, Number> chart, FireModeReport report) {
        val series = new XYChart.Series<Number, Number>();

        series.setName(SHIFTED_MAX_ALLOWED_TEMPERATURE_TEXT);
        ChartUtils.addPointsToSeries(series, getMaintainedMaxAllowedTemperature(report)
                .getShiftedValue(report.getProperties().getBoundsShift().getMaxAllowedTemperatureShift()));
        chart.getData().add(series);
        series.getNode().setId(ElementIds.SHIFTED_BOUND);
    }

    private void addMinAllowedTemperatureLine(LineChart<Number, Number> chart, FireModeReport report) {
        val series = new XYChart.Series<Number, Number>();

        series.setName(MIN_ALLOWED_TEMPERATURE_TEXT);
        ChartUtils.addPointsToSeries(series, getMaintainedMinAllowedTemperature(report));
        chart.getData().add(series);
        series.getNode().setId(ElementIds.FIREMODE_MIN_TEMPERATURE_LINE);
    }

    private void addShiftedMinAllowedTemperatureLine(LineChart<Number, Number> chart, FireModeReport report) {
        val series = new XYChart.Series<Number, Number>();

        series.setName(SHIFTED_MIN_ALLOWED_TEMPERATURE_TEXT);
        ChartUtils.addPointsToSeries(series, getMaintainedMinAllowedTemperature(report)
                .getShiftedValue(report.getProperties().getBoundsShift().getMinAllowedTemperatureShift()));
        chart.getData().add(series);
        series.getNode().setId(ElementIds.SHIFTED_BOUND);
    }

    private void addThermocoupleMeanTemperatureLine(LineChart<Number, Number> chart, FireModeReport report) {
        val series = new XYChart.Series<Number, Number>();

        series.setName(MEAN_TEMPERATURE_TEXT);
        ChartUtils.addPointsToSeries(series, getMaintainedThermocoupleMeanTemperature(report));
        chart.getData().add(series);
        series.getNode().setId(ElementIds.DEFAULT_MEAN_LINE);
    }

    private void addThermocoupleTemperatureLines(LineChart<Number, Number> chart, FireModeReport report) {
        val thermocoupleTemperatures = getMaintainedThermocoupleTemperatures(report);

        for (int i = 0; i < thermocoupleTemperatures.size(); i++) {

            val thermocoupleSeries = new XYChart.Series<Number, Number>();
            val thermocoupleTemperature = thermocoupleTemperatures.get(i);

            thermocoupleSeries.setName(THERMOCOUPLE_TEXT + (i + 1));
            ChartUtils.addPointsToSeries(thermocoupleSeries, thermocoupleTemperature);
            chart.getData().add(thermocoupleSeries);
            thermocoupleSeries.getNode().setId(ElementIds.FIREMODE_THERMOCOUPLE_TEMPERATURE_LINE);
        }
    }

}

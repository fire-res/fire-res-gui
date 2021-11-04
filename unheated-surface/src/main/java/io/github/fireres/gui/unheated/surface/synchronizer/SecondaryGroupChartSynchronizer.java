package io.github.fireres.gui.unheated.surface.synchronizer;

import io.github.fireres.gui.framework.model.ElementIds;
import io.github.fireres.gui.framework.synchronizer.ChartSynchronizer;
import io.github.fireres.gui.framework.util.ChartUtils;
import io.github.fireres.unheated.surface.report.UnheatedSurfaceReport;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import lombok.val;
import org.springframework.stereotype.Component;

@Component
public class SecondaryGroupChartSynchronizer implements ChartSynchronizer<UnheatedSurfaceReport> {

    public static final String MEAN_TEMPERATURE_TEXT = "Среднее значение температуры";
    public static final String MAX_THERMOCOUPLE_TEMPERATURE_TEXT = "Предельное значение температуры термопар";
    public static final String SHIFTED_MAX_THERMOCOUPLE_TEMPERATURE_TEXT = "Предельное значение температуры термопар (со смещением)";
    public static final String THERMOCOUPLE_TEXT = "Термопара - ";

    public void synchronize(LineChart<Number, Number> chart, UnheatedSurfaceReport report) {
        chart.getData().clear();

        addThermocoupleTemperatureLines(chart, report);
        addMeanTemperatureLine(chart, report);
        addThermocoupleBoundLine(chart, report);
        addShiftedThermocoupleBoundLine(chart, report);

        ChartUtils.addLegendSymbolId(chart, MEAN_TEMPERATURE_TEXT, ElementIds.DEFAULT_MEAN_LINE_LEGEND_SYMBOL);
    }

    private void addThermocoupleTemperatureLines(LineChart<Number, Number> chart, UnheatedSurfaceReport report) {
        for (int i = 0; i < report.getThermocoupleTemperatures().size(); i++) {

            val thermocoupleSeries = new XYChart.Series<Number, Number>();
            val thermocoupleTemperature = report.getThermocoupleTemperatures().get(i);

            thermocoupleSeries.setName(THERMOCOUPLE_TEXT + (i + 1));
            ChartUtils.addPointsToSeries(thermocoupleSeries, thermocoupleTemperature);
            chart.getData().add(thermocoupleSeries);
            thermocoupleSeries.getNode().setId(ElementIds.SECOND_THERMOCOUPLE_GROUP_THERMOCOUPLE_TEMPERATURE_LINE);
        }
    }

    private void addThermocoupleBoundLine(LineChart<Number, Number> chart, UnheatedSurfaceReport report) {
        val series = new XYChart.Series<Number, Number>();

        series.setName(MAX_THERMOCOUPLE_TEMPERATURE_TEXT);
        ChartUtils.addPointsToSeries(series, report.getMaxAllowedThermocoupleTemperature());
        chart.getData().add(series);
        series.getNode().setId(ElementIds.SECOND_THERMOCOUPLE_GROUP_THERMOCOUPLE_MAX_TEMPERATURE_LINE);
    }

    private void addShiftedThermocoupleBoundLine(LineChart<Number, Number> chart, UnheatedSurfaceReport report) {
        val series = new XYChart.Series<Number, Number>();

        series.setName(SHIFTED_MAX_THERMOCOUPLE_TEMPERATURE_TEXT);
        ChartUtils.addPointsToSeries(series, report.getMaxAllowedThermocoupleTemperature()
                .getShiftedValue(report.getProperties().getBoundsShift().getMaxAllowedThermocoupleTemperatureShift()));
        chart.getData().add(series);
        series.getNode().setId(ElementIds.SHIFTED_BOUND);
    }

    private void addMeanTemperatureLine(LineChart<Number, Number> chart, UnheatedSurfaceReport report) {
        val series = new XYChart.Series<Number, Number>();

        series.setName(MEAN_TEMPERATURE_TEXT);
        ChartUtils.addPointsToSeries(series, report.getMeanTemperature());
        chart.getData().add(series);
        series.getNode().setId(ElementIds.DEFAULT_MEAN_LINE);
    }
}

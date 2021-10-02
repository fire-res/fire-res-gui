package io.github.fireres.gui.synchronizer.impl;

import io.github.fireres.gui.synchronizer.ChartSynchronizer;
import io.github.fireres.heatflow.report.HeatFlowReport;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import lombok.val;
import org.springframework.stereotype.Component;

import static io.github.fireres.gui.model.ElementIds.DEFAULT_MEAN_LINE;
import static io.github.fireres.gui.model.ElementIds.DEFAULT_MEAN_LINE_LEGEND_SYMBOL;
import static io.github.fireres.gui.model.ElementIds.HEAT_FLOW_MAX_ALLOWED_FLOW_LINE;
import static io.github.fireres.gui.model.ElementIds.HEAT_FLOW_SENSORS_LINE;
import static io.github.fireres.gui.model.ElementIds.SHIFTED_BOUND;
import static io.github.fireres.gui.util.ChartUtils.addLegendSymbolId;
import static io.github.fireres.gui.util.ChartUtils.addPointsToSeries;

@Component
public class HeatFlowChartSynchronizer implements ChartSynchronizer<HeatFlowReport> {

    public static final String MAX_ALLOWED_FLOW_TEXT = "Предельное значение теплового потока";
    public static final String SHIFTED_MAX_ALLOWED_FLOW_TEXT = "Предельное значение теплового потока (со смещением)";
    public static final String MEAN_FLOW_TEXT = "Среднее значение теплового потока";
    public static final String SENSOR_TEMPERATURE_TEXT = "Приемник теплового потока - ";

    @Override
    public void synchronize(LineChart<Number, Number> chart, HeatFlowReport report) {
        chart.getData().clear();

        addSensorsLine(chart, report);
        addMeanHeatFlowLine(chart, report);
        addMaxAllowedHeatFlowLine(chart, report);
        addShiftedMaxAllowedHeatFlowLine(chart, report);

        addLegendSymbolId(chart, MEAN_FLOW_TEXT, DEFAULT_MEAN_LINE_LEGEND_SYMBOL);
    }

    private void addMaxAllowedHeatFlowLine(LineChart<Number, Number> chart, HeatFlowReport report) {
        val series = new XYChart.Series<Number, Number>();

        series.setName(MAX_ALLOWED_FLOW_TEXT);
        addPointsToSeries(series, report.getBound());
        chart.getData().add(series);
        series.getNode().setId(HEAT_FLOW_MAX_ALLOWED_FLOW_LINE);
    }

    private void addShiftedMaxAllowedHeatFlowLine(LineChart<Number, Number> chart, HeatFlowReport report) {
        val series = new XYChart.Series<Number, Number>();

        series.setName(SHIFTED_MAX_ALLOWED_FLOW_TEXT);
        addPointsToSeries(series, report.getBound()
                .getShiftedValue(report.getProperties().getBoundsShift().getMaxAllowedFlowShift()));
        chart.getData().add(series);
        series.getNode().setId(SHIFTED_BOUND);
    }

    private void addMeanHeatFlowLine(LineChart<Number, Number> chart, HeatFlowReport report) {
        val series = new XYChart.Series<Number, Number>();

        series.setName(MEAN_FLOW_TEXT);
        addPointsToSeries(series, report.getMeanTemperature());
        chart.getData().add(series);
        series.getNode().setId(DEFAULT_MEAN_LINE);
    }

    private void addSensorsLine(LineChart<Number, Number> chart, HeatFlowReport report) {
        for (int i = 0; i < report.getSensorTemperatures().size(); i++) {
            val sensorSeries = new XYChart.Series<Number, Number>();
            val sensorTemperature = report.getSensorTemperatures().get(i);

            sensorSeries.setName(SENSOR_TEMPERATURE_TEXT + (i + 1));
            addPointsToSeries(sensorSeries, sensorTemperature);
            chart.getData().add(sensorSeries);
            sensorSeries.getNode().setId(HEAT_FLOW_SENSORS_LINE);
        }
    }

}

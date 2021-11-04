package io.github.fireres.gui.excess.pressure.synchronizer;

import io.github.fireres.excess.pressure.report.ExcessPressureReport;
import io.github.fireres.gui.framework.model.ElementIds;
import io.github.fireres.gui.framework.synchronizer.ChartSynchronizer;
import io.github.fireres.gui.framework.util.ChartUtils;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import lombok.val;
import org.springframework.stereotype.Component;

@Component
public class ExcessPressureChartSynchronizer implements ChartSynchronizer<ExcessPressureReport> {

    private static final String Y_AXIS_LABEL_TEMPLATE = "Избыточное давление, %s±ΔПа";

    public static final String MIN_ALLOWED_PRESSURE_TEXT = "Минимальный допуск избыточного давления";
    public static final String SHIFTED_MIN_ALLOWED_PRESSURE_TEXT = "Минимальный допуск избыточного давления (со смещением)";
    public static final String MAX_ALLOWED_PRESSURE_TEXT = "Максимальный допуск избыточного давления";
    public static final String SHIFTED_MAX_ALLOWED_PRESSURE_TEXT = "Максимальный допуск избыточного давления (со смещением)";
    public static final String PRESSURE_TEXT = "Избыточное давление";

    @Override
    public void synchronize(LineChart<Number, Number> chart, ExcessPressureReport report) {
        chart.getData().clear();

        addMinAllowedPressureLine(chart, report);
        addShiftedMinAllowedPressureLine(chart, report);
        addMaxAllowedPressureLine(chart, report);
        addShiftedMaxAllowedPressureLine(chart, report);
        addPressureLine(chart, report);
        setXAxisName(chart, report);
    }

    private void addMinAllowedPressureLine(LineChart<Number, Number> chart, ExcessPressureReport report) {
        val series = new XYChart.Series<Number, Number>();

        series.setName(MIN_ALLOWED_PRESSURE_TEXT);
        ChartUtils.addPointsToSeries(series, report.getMinAllowedPressure());
        chart.getData().add(series);
        series.getNode().setId(ElementIds.EXCESS_PRESSURE_MIN_ALLOWED_PRESSURE_LINE);
    }

    private void addShiftedMinAllowedPressureLine(LineChart<Number, Number> chart, ExcessPressureReport report) {
        val series = new XYChart.Series<Number, Number>();

        series.setName(SHIFTED_MIN_ALLOWED_PRESSURE_TEXT);
        ChartUtils.addPointsToSeries(series, report.getMinAllowedPressure()
                .getShiftedValue(report.getProperties().getBoundsShift().getMinAllowedPressureShift()));
        chart.getData().add(series);
        series.getNode().setId(ElementIds.SHIFTED_BOUND);
    }

    private void addMaxAllowedPressureLine(LineChart<Number, Number> chart, ExcessPressureReport report) {
        val series = new XYChart.Series<Number, Number>();

        series.setName(MAX_ALLOWED_PRESSURE_TEXT);
        ChartUtils.addPointsToSeries(series, report.getMaxAllowedPressure());
        chart.getData().add(series);
        series.getNode().setId(ElementIds.EXCESS_PRESSURE_MAX_ALLOWED_PRESSURE_LINE);
    }

    private void addShiftedMaxAllowedPressureLine(LineChart<Number, Number> chart, ExcessPressureReport report) {
        val series = new XYChart.Series<Number, Number>();

        series.setName(SHIFTED_MAX_ALLOWED_PRESSURE_TEXT);
        ChartUtils.addPointsToSeries(series, report.getMaxAllowedPressure()
                .getShiftedValue(report.getProperties().getBoundsShift().getMaxAllowedPressureShift()));
        chart.getData().add(series);
        series.getNode().setId(ElementIds.SHIFTED_BOUND);
    }

    private void addPressureLine(LineChart<Number, Number> chart, ExcessPressureReport report) {
        val series = new XYChart.Series<Number, Number>();

        series.setName(PRESSURE_TEXT);
        ChartUtils.addPointsToSeries(series, report.getPressure());
        chart.getData().add(series);
        series.getNode().setId(ElementIds.EXCESS_PRESSURE_LINE);
    }

    private void setXAxisName(LineChart<Number, Number> chart, ExcessPressureReport report) {
        chart.getYAxis().setLabel(String.format(Y_AXIS_LABEL_TEMPLATE, report.getBasePressure()));
    }

}

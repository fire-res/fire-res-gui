package io.github.fireres.gui.util;

import com.sun.javafx.charts.Legend;
import io.github.fireres.core.model.Point;
import io.github.fireres.core.model.PointSequence;
import javafx.scene.Node;
import javafx.scene.chart.Chart;
import javafx.scene.chart.XYChart;
import lombok.experimental.UtilityClass;
import lombok.val;

import java.util.List;
import java.util.Optional;

@UtilityClass
public class ChartUtils {

    public static void addPointsToSeries(XYChart.Series<Number, Number> series, PointSequence<? extends Point<? extends Number>> points) {
        points.getValue().forEach(point -> series.getData().add(new XYChart.Data<>(point.getTime(), point.getValue())));
    }

    public static void addPointsToSeries(XYChart.Series<Number, Number> series, List<? extends Point<? extends Number>> points) {
        points.forEach(point -> series.getData().add(new XYChart.Data<>(point.getTime(), point.getValue())));
    }

    public static void addLegendSymbolId(Chart chart, String expectedText, String symbolId) {
        val legend = getChartLegend(chart).orElseThrow();

        legend.getItems().forEach(item -> {
            if (item.getText().equals(expectedText)) {
                item.getSymbol().setId(symbolId);
            }
        });
    }

    public static Optional<Legend> getChartLegend(Chart chart) {
        for (Node node : chart.getChildrenUnmodifiable()) {
            if (node instanceof Legend) {
                return Optional.of((Legend) node);
            }
        }

        return Optional.empty();
    }

}

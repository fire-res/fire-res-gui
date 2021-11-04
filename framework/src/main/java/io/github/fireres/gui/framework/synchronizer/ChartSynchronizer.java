package io.github.fireres.gui.framework.synchronizer;

import io.github.fireres.core.model.Report;
import javafx.scene.chart.LineChart;

public interface ChartSynchronizer<R extends Report<?>> {

    void synchronize(LineChart<Number, Number> chart, R report);

}

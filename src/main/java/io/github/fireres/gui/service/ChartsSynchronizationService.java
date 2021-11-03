package io.github.fireres.gui.service;

import io.github.fireres.excess.pressure.report.ExcessPressureReport;
import io.github.fireres.firemode.report.FireModeReport;
import io.github.fireres.heatflow.report.HeatFlowReport;
import io.github.fireres.unheated.surface.report.UnheatedSurfaceReport;
import javafx.scene.chart.LineChart;

public interface ChartsSynchronizationService {

    void syncFireModeChart(LineChart<Number, Number> chart, FireModeReport report);

    void syncExcessPressureChart(LineChart<Number, Number> chart, ExcessPressureReport report);

    void syncHeatFlowChart(LineChart<Number, Number> chart, HeatFlowReport report);

    void syncPrimaryGroupChart(LineChart<Number, Number> chart, UnheatedSurfaceReport report);

    void syncSecondaryGroupChart(LineChart<Number, Number> chart, UnheatedSurfaceReport report);

}

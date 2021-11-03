package io.github.fireres.gui.service.impl;

import io.github.fireres.excess.pressure.report.ExcessPressureReport;
import io.github.fireres.firemode.report.FireModeReport;
import io.github.fireres.gui.service.ChartsSynchronizationService;
import io.github.fireres.gui.synchronizer.impl.ExcessPressureChartSynchronizer;
import io.github.fireres.gui.synchronizer.impl.FireModeChartSynchronizer;
import io.github.fireres.gui.synchronizer.impl.HeatFlowChartSynchronizer;
import io.github.fireres.gui.synchronizer.impl.PrimaryGroupChartSynchronizer;
import io.github.fireres.gui.synchronizer.impl.SecondaryGroupChartSynchronizer;
import io.github.fireres.heatflow.report.HeatFlowReport;
import io.github.fireres.unheated.surface.report.UnheatedSurfaceReport;
import javafx.scene.chart.LineChart;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChartsSynchronizationServiceImpl implements ChartsSynchronizationService {

    private final FireModeChartSynchronizer fireModeChartSynchronizer;
    private final ExcessPressureChartSynchronizer excessPressureChartSynchronizer;
    private final HeatFlowChartSynchronizer heatFlowChartSynchronizer;
    private final PrimaryGroupChartSynchronizer primaryGroupChartSynchronizer;
    private final SecondaryGroupChartSynchronizer secondaryGroupChartSynchronizer;

    @Override
    public void syncFireModeChart(LineChart<Number, Number> chart, FireModeReport report) {
        fireModeChartSynchronizer.synchronize(chart, report);
    }

    @Override
    public void syncExcessPressureChart(LineChart<Number, Number> chart, ExcessPressureReport report) {
        excessPressureChartSynchronizer.synchronize(chart, report);
    }

    @Override
    public void syncHeatFlowChart(LineChart<Number, Number> chart, HeatFlowReport report) {
        heatFlowChartSynchronizer.synchronize(chart, report);
    }

    @Override
    public void syncPrimaryGroupChart(LineChart<Number, Number> chart, UnheatedSurfaceReport report) {
        primaryGroupChartSynchronizer.synchronize(chart, report);
    }

    @Override
    public void syncSecondaryGroupChart(LineChart<Number, Number> chart, UnheatedSurfaceReport report) {
        secondaryGroupChartSynchronizer.synchronize(chart, report);
    }

}

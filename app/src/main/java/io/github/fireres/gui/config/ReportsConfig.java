package io.github.fireres.gui.config;

import io.github.fireres.core.model.ReportType;
import io.github.fireres.core.service.ReportCreatorService;
import io.github.fireres.excel.report.ExcelReportsBuilder;
import io.github.fireres.excel.report.ExcessPressureExcelReportsBuilder;
import io.github.fireres.excel.report.FireModeExcelReportsBuilder;
import io.github.fireres.excel.report.HeatFlowExcelReportsBuilder;
import io.github.fireres.excel.report.UnheatedSurfaceExcelReportsBuilder;
import io.github.fireres.excess.pressure.report.ExcessPressureReport;
import io.github.fireres.excess.pressure.service.ExcessPressureService;
import io.github.fireres.firemode.report.FireModeReport;
import io.github.fireres.firemode.service.FireModeService;
import io.github.fireres.gui.excess.pressure.controller.ExcessPressure;
import io.github.fireres.gui.firemode.controller.FireMode;
import io.github.fireres.gui.framework.controller.ReportTab;
import io.github.fireres.gui.heatflow.controller.HeatFlow;
import io.github.fireres.gui.unheated.surface.controller.UnheatedSurface;
import io.github.fireres.heatflow.report.HeatFlowReport;
import io.github.fireres.heatflow.service.HeatFlowService;
import io.github.fireres.unheated.surface.report.UnheatedSurfaceReport;
import io.github.fireres.unheated.surface.service.UnheatedSurfaceService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

import static io.github.fireres.excess.pressure.report.ExcessPressureReportType.EXCESS_PRESSURE;
import static io.github.fireres.firemode.report.FireModeReportType.FIRE_MODE;
import static io.github.fireres.heatflow.report.HeatFlowReportType.HEAT_FLOW;
import static io.github.fireres.unheated.surface.report.UnheatedSurfaceReportType.UNHEATED_SURFACE;

@Configuration
public class ReportsConfig {

    @Bean
    public Map<Class<?>, ReportCreatorService<?, ?>> reportCreators(
            FireModeService fireModeService,
            ExcessPressureService excessPressureService,
            HeatFlowService heatFlowService,
            UnheatedSurfaceService unheatedSurfaceService
    ) {
        return Map.of(
                FireModeReport.class, fireModeService,
                ExcessPressureReport.class, excessPressureService,
                HeatFlowReport.class, heatFlowService,
                UnheatedSurfaceReport.class, unheatedSurfaceService
        );
    }

    @Bean
    public List<Class<? extends ReportTab>> reportTabClasses() {
        return List.of(
                ExcessPressure.class,
                FireMode.class,
                HeatFlow.class,
                UnheatedSurface.class
        );
    }

    @Bean
    public List<ReportType> reportTypes() {
        return List.of(
                EXCESS_PRESSURE,
                FIRE_MODE,
                HEAT_FLOW,
                UNHEATED_SURFACE
        );
    }

    @Bean
    public Map<String, Integer> tabOrder() {
        return Map.of(
                FIRE_MODE.getDescription(), 0,
                EXCESS_PRESSURE.getDescription(), 1,
                HEAT_FLOW.getDescription(), 2,
                UNHEATED_SURFACE.getDescription(), 3
        );
    }

    @Bean
    public Map<ReportType, ExcelReportsBuilder> reportsBuilders(
            ExcessPressureExcelReportsBuilder excessPressureExcelReportsBuilder,
            FireModeExcelReportsBuilder fireModeExcelReportsBuilder,
            HeatFlowExcelReportsBuilder heatFlowExcelReportsBuilder,
            UnheatedSurfaceExcelReportsBuilder unheatedSurfaceExcelReportsBuilder
    ) {
        return Map.of(
                EXCESS_PRESSURE, excessPressureExcelReportsBuilder,
                FIRE_MODE, fireModeExcelReportsBuilder,
                HEAT_FLOW, heatFlowExcelReportsBuilder,
                UNHEATED_SURFACE, unheatedSurfaceExcelReportsBuilder
        );
    }

}

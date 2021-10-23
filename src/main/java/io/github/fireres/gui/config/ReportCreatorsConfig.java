package io.github.fireres.gui.config;

import io.github.fireres.core.service.ReportCreatorService;
import io.github.fireres.excess.pressure.report.ExcessPressureReport;
import io.github.fireres.excess.pressure.service.ExcessPressureService;
import io.github.fireres.firemode.report.FireModeReport;
import io.github.fireres.firemode.service.FireModeService;
import io.github.fireres.heatflow.report.HeatFlowReport;
import io.github.fireres.heatflow.service.HeatFlowService;
import io.github.fireres.unheated.surface.report.UnheatedSurfaceReport;
import io.github.fireres.unheated.surface.service.UnheatedSurfaceService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class ReportCreatorsConfig {

    @Bean
    public Map<Class<?>, ReportCreatorService<?>> reportCreators(
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

}

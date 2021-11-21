package io.github.fireres.gui.config;

import io.github.fireres.excel.core.builder.ExcelSheetsBuilder;
import io.github.fireres.excel.core.config.AbstractExcelConfig;
import io.github.fireres.excel.excess.pressure.builder.ExcessPressureSheetsBuilder;
import io.github.fireres.excel.fire.mode.builder.FireModeSheetsBuilder;
import io.github.fireres.excel.heat.flow.builder.HeatFlowSheetsBuilder;
import io.github.fireres.excel.unheated.surface.builder.UnheatedSurfaceSheetsBuilder;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class ExcelReport extends AbstractExcelConfig {

    private static final Map<Class<? extends ExcelSheetsBuilder>, Integer> SHEETS_ORDER = Map.of(
            FireModeSheetsBuilder.class, 0,
            ExcessPressureSheetsBuilder.class, 1,
            HeatFlowSheetsBuilder.class, 2,
            UnheatedSurfaceSheetsBuilder.class, 3
    );

    @Override
    protected int getOrder(Class<? extends ExcelSheetsBuilder> builderClass) {
        return SHEETS_ORDER.get(builderClass);
    }

}

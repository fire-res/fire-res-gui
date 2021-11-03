package io.github.fireres.gui.initializer.report;

import io.github.fireres.gui.controller.PresetContainer;
import io.github.fireres.gui.controller.heat.flow.HeatFlow;
import io.github.fireres.gui.initializer.Initializer;
import io.github.fireres.gui.preset.impl.HeatFlowPresetApplier;
import io.github.fireres.heatflow.model.HeatFlowPoint;
import io.github.fireres.heatflow.properties.HeatFlowProperties;
import io.github.fireres.heatflow.service.HeatFlowService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static io.github.fireres.gui.synchronizer.impl.HeatFlowChartSynchronizer.MAX_ALLOWED_FLOW_TEXT;
import static java.util.Collections.singletonList;

@Component
@RequiredArgsConstructor
public class HeatFlowInitializer implements Initializer<HeatFlow> {

    private final HeatFlowPresetApplier presetApplier;
    private final HeatFlowService heatFlowService;

    @Override
    public void initialize(HeatFlow heatFlow) {
        applyDefaultPreset(heatFlow);
        initializeFunctionParams(heatFlow);
        initializeBoundsShiftParams(heatFlow);
    }

    private void applyDefaultPreset(HeatFlow heatFlow) {
        presetApplier.apply(heatFlow, ((PresetContainer) heatFlow.getParent()).getPreset());
    }

    private void initializeFunctionParams(HeatFlow heatFlow) {
        heatFlow.getFunctionParams().setInterpolationService(heatFlowService);

        heatFlow.getFunctionParams().setNodesToBlockOnUpdate(singletonList(heatFlow.getParamsVbox()));

        heatFlow.getFunctionParams().setInterpolationPointConstructor(
                (time, value) -> new HeatFlowPoint(time, value.doubleValue()));
    }

    private void initializeBoundsShiftParams(HeatFlow heatFlow) {
        heatFlow.getBoundsShiftParams().addBoundShift(
                MAX_ALLOWED_FLOW_TEXT,
                singletonList(heatFlow.getParamsVbox()),
                properties -> ((HeatFlowProperties) properties).getBoundsShift().getMaxAllowedFlowShift(),
                point -> heatFlowService.addMaxAllowedFlowShift(heatFlow.getReport(), (HeatFlowPoint) point),
                point -> heatFlowService.removeMaxAllowedFlowShift(heatFlow.getReport(), (HeatFlowPoint) point),
                (integer, number) -> new HeatFlowPoint(integer, number.doubleValue())
        );
    }

}

package io.github.fireres.gui.heatflow.preset;

import com.rits.cloning.Cloner;
import io.github.fireres.gui.framework.preset.BoundsShiftApplier;
import io.github.fireres.gui.framework.preset.FunctionFormApplier;
import io.github.fireres.gui.framework.preset.Preset;
import io.github.fireres.gui.framework.preset.PresetApplier;
import io.github.fireres.gui.heatflow.config.properties.HeatFlowBoundProperties;
import io.github.fireres.gui.heatflow.config.properties.HeatFlowSensorsProperties;
import io.github.fireres.gui.heatflow.controller.HeatFlow;
import io.github.fireres.heatflow.properties.HeatFlowProperties;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HeatFlowPresetApplier implements PresetApplier<HeatFlow> {

    private final Cloner cloner;
    private final FunctionFormApplier functionFormApplier;
    private final BoundsShiftApplier boundsShiftApplier;
    private final HeatFlowSensorsProperties sensorsProperties;
    private final HeatFlowBoundProperties boundProperties;

    @Override
    public void apply(HeatFlow heatFlow, Preset preset) {
        val presetProperties = cloner.deepClone(preset
                .findFirst(HeatFlowProperties.class)
                .orElse(new HeatFlowProperties()));

        val heatFlowParameters = heatFlow.getHeatFlowParams();

        setSensorsCount(heatFlowParameters.getSensors(), presetProperties);
        setHeatFlowBound(heatFlowParameters.getBound(), presetProperties);

        functionFormApplier.apply(heatFlow.getFunctionParams(), presetProperties.getFunctionForm());
        boundsShiftApplier.apply(heatFlow.getBoundsShiftParams(), presetProperties.getBoundsShift());

        heatFlow.createReport(presetProperties);
    }

    private void setSensorsCount(Spinner<Integer> sensorsCount, HeatFlowProperties properties) {
        sensorsCount.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(
                sensorsProperties.getMinValue(),
                sensorsProperties.getMaxValue(),
                properties.getSensorCount()));
    }

    private void setHeatFlowBound(Spinner<Double> heatFlow, HeatFlowProperties properties) {
        heatFlow.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(
                boundProperties.getMinValue(),
                boundProperties.getMaxValue(),
                properties.getBound(),
                boundProperties.getIncrement()));
    }
}

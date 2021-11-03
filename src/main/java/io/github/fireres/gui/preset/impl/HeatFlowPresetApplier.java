package io.github.fireres.gui.preset.impl;

import com.rits.cloning.Cloner;
import io.github.fireres.gui.config.properties.heatflow.HeatFlowBoundProperties;
import io.github.fireres.gui.config.properties.heatflow.HeatFlowSensorsProperties;
import io.github.fireres.gui.controller.heat.flow.HeatFlow;
import io.github.fireres.gui.preset.FunctionFormApplier;
import io.github.fireres.gui.preset.Preset;
import io.github.fireres.gui.preset.PresetApplier;
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
    private final HeatFlowSensorsProperties sensorsProperties;
    private final HeatFlowBoundProperties boundProperties;

    @Override
    public void apply(HeatFlow heatFlow, Preset preset) {
        val presetProperties = cloner.deepClone(preset
                .getHeatFlowProperties()
                .orElse(new HeatFlowProperties()));

        val heatFlowParameters = heatFlow.getHeatFlowParams();

        setSensorsCount(heatFlowParameters.getSensors(), presetProperties);
        setHeatFlowBound(heatFlowParameters.getBound(), presetProperties);

        functionFormApplier.apply(heatFlow.getFunctionParams(), presetProperties.getFunctionForm());

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

package io.github.fireres.gui.preset.impl;

import com.rits.cloning.Cloner;
import io.github.fireres.gui.controller.unheated.surface.groups.first.FirstGroup;
import io.github.fireres.gui.preset.FunctionFormApplier;
import io.github.fireres.gui.preset.Preset;
import io.github.fireres.gui.preset.PresetApplier;
import io.github.fireres.unheated.surface.properties.UnheatedSurfaceProperties;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UnheatedSurfaceFirstGroupPresetApplier implements PresetApplier<FirstGroup> {

    private static final Integer THERMOCOUPLES_NUMBER_MIN = 1;
    private static final Integer THERMOCOUPLES_NUMBER_MAX = 100;

    private final Cloner cloner;
    private final FunctionFormApplier functionFormApplier;

    @Override
    public void apply(FirstGroup firstGroup, Preset preset) {
        val sampleProperties = firstGroup.getSample().getSampleProperties();
        val presetProperties = cloner.deepClone(preset.getProperties(UnheatedSurfaceProperties.class).getFirstGroup());

        sampleProperties.getReportPropertiesByClass(UnheatedSurfaceProperties.class)
                .orElseThrow()
                .setFirstGroup(presetProperties);

        setThermocouplesCount(
                firstGroup.getFirstGroupParams().getThermocouples(),
                presetProperties.getThermocoupleCount());

        functionFormApplier.apply(firstGroup.getFunctionParams(), presetProperties.getFunctionForm());
    }

    private void setThermocouplesCount(Spinner<Integer> thermocouplesCount, Integer thermocouplesCountValue) {
        thermocouplesCount.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(
                THERMOCOUPLES_NUMBER_MIN,
                THERMOCOUPLES_NUMBER_MAX,
                thermocouplesCountValue));
    }
}

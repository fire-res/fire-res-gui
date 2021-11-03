package io.github.fireres.gui.preset.impl;

import com.rits.cloning.Cloner;
import io.github.fireres.gui.controller.unheated.surface.groups.secondary.SecondaryGroup;
import io.github.fireres.gui.preset.FunctionFormApplier;
import io.github.fireres.gui.preset.Preset;
import io.github.fireres.gui.preset.PresetApplier;
import io.github.fireres.unheated.surface.properties.UnheatedSurfaceProperties;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UnheatedSurfaceSecondaryGroupPresetApplier implements PresetApplier<SecondaryGroup> {

    private static final Integer THERMOCOUPLES_NUMBER_MIN = 1;
    private static final Integer THERMOCOUPLES_NUMBER_MAX = 100;

    private static final Integer BOUND_MIN = 2;
    private static final Integer BOUND_MAX = 10000;
    private static final Integer BOUND_INCREMENT = 100;

    private final Cloner cloner;
    private final FunctionFormApplier functionFormApplier;

    @Override
    public void apply(SecondaryGroup secondaryGroup, Preset preset) {
        val properties = findProperties(preset, secondaryGroup.getPropertiesId());

        setThermocouplesCount(
                secondaryGroup.getSecondGroupParams().getThermocouples(),
                properties.getThermocoupleCount());

        setBound(
                secondaryGroup.getSecondGroupParams().getBound(),
                properties.getBound());

        functionFormApplier.apply(secondaryGroup.getFunctionParams(), properties.getFunctionForm());

        secondaryGroup.createReport(properties);
    }

    private UnheatedSurfaceProperties findProperties(Preset preset, UUID propertiesId) {
        return cloner.deepClone(preset.getUnheatedSurfaceProperties().stream()
                .filter(properties -> properties.getId().equals(propertiesId))
                .findFirst()
                .orElseThrow());
    }

    private void setThermocouplesCount(Spinner<Integer> thermocouplesCount, Integer thermocouplesCountValue) {
        thermocouplesCount.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(
                THERMOCOUPLES_NUMBER_MIN,
                THERMOCOUPLES_NUMBER_MAX,
                thermocouplesCountValue));
    }

    private void setBound(Spinner<Integer> bound, Integer boundValue) {
        bound.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(
                BOUND_MIN,
                BOUND_MAX,
                boundValue,
                BOUND_INCREMENT));
    }

}

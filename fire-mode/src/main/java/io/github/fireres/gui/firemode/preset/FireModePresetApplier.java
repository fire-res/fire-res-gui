package io.github.fireres.gui.firemode.preset;

import com.rits.cloning.Cloner;
import io.github.fireres.gui.firemode.config.properties.FireModeTemperatureMaintainingProperties;
import io.github.fireres.gui.firemode.config.properties.FireModeThermocoupleCountProperties;
import io.github.fireres.gui.firemode.controller.FireMode;
import io.github.fireres.gui.framework.component.FireResChoiceBox;
import io.github.fireres.gui.framework.preset.BoundsShiftApplier;
import io.github.fireres.gui.framework.preset.Preset;
import io.github.fireres.firemode.model.FireModeType;
import io.github.fireres.firemode.properties.FireModeProperties;
import io.github.fireres.gui.framework.preset.FunctionFormApplier;
import io.github.fireres.gui.framework.preset.PresetApplier;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FireModePresetApplier implements PresetApplier<FireMode> {

    private final Cloner cloner;
    private final FunctionFormApplier functionFormApplier;
    private final BoundsShiftApplier boundsShiftApplier;
    private final FireModeThermocoupleCountProperties thermocoupleCountProperties;
    private final FireModeTemperatureMaintainingProperties temperatureMaintainingProperties;

    @Override
    public void apply(FireMode fireMode, Preset preset) {
        val presetProperties = cloner.deepClone(preset
                .findFirst(FireModeProperties.class)
                .orElse(new FireModeProperties()));

        val fireModeParams = fireMode.getFireModeParams();

        setThermocoupleCount(fireModeParams.getThermocouples(), presetProperties);
        setFireModeType(fireModeParams.getFireModeType(), presetProperties);
        setStandardTemperatureMaintaining(fireModeParams.getTemperatureMaintaining(), presetProperties);
        setShowBounds(fireModeParams.getShowBounds(), presetProperties);
        setShowMeanTemperature(fireModeParams.getShowMeanTemperature(), presetProperties);

        functionFormApplier.apply(fireMode.getFunctionParams(), presetProperties.getFunctionForm());
        boundsShiftApplier.apply(fireMode.getBoundsShiftParams(), presetProperties.getBoundsShift());

        fireMode.createReport(presetProperties);
    }

    private void setThermocoupleCount(Spinner<Integer> thermocoupleSpinner, FireModeProperties properties) {
        thermocoupleSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(
                thermocoupleCountProperties.getMinValue(),
                thermocoupleCountProperties.getMaxValue(),
                properties.getThermocoupleCount()));
    }

    private void setFireModeType(FireResChoiceBox<FireModeType> fireModeType, FireModeProperties properties) {
        fireModeType.setValueWithoutListener(properties.getFireModeType());
    }

    private void setStandardTemperatureMaintaining(Spinner<Integer> standardTemperatureMaintaining,
                                                   FireModeProperties properties) {

        standardTemperatureMaintaining.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(
                temperatureMaintainingProperties.getMinValue(),
                temperatureMaintainingProperties.getMaxValue(),
                properties.getTemperaturesMaintaining() != null
                        ? properties.getTemperaturesMaintaining()
                        : 0));
    }

    private void setShowBounds(CheckBox showBounds, FireModeProperties properties) {
        showBounds.setSelected(properties.getShowBounds());
    }

    private void setShowMeanTemperature(CheckBox showMeanTemperature, FireModeProperties properties) {
        showMeanTemperature.setSelected(properties.getShowMeanTemperature());
    }
}

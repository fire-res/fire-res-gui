package io.github.fireres.gui.framework.initializer.modal;

import io.github.fireres.gui.framework.controller.modal.preset.management.PresetItem;
import io.github.fireres.gui.framework.controller.modal.preset.management.PresetManagementModalWindow;
import io.github.fireres.gui.framework.initializer.Initializer;
import io.github.fireres.gui.framework.service.FxmlLoadService;
import io.github.fireres.gui.framework.service.PresetService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PresetManagementModalWindowInitializer implements Initializer<PresetManagementModalWindow> {

    private final PresetService presetService;
    private final FxmlLoadService fxmlLoadService;

    @Override
    public void initialize(PresetManagementModalWindow window) {
        val availablePresets = presetService.getAvailablePresets();

        availablePresets.forEach(preset -> {
            val presetItem = fxmlLoadService.loadComponent(PresetItem.class, window);

            presetItem.getPresetDescription().setText(preset.getDescription());
            presetItem.setPreset(preset);

            window.getPresetsVbox().getChildren().add(presetItem.getComponent());
        });
    }

}

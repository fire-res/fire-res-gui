package io.github.fireres.gui.initializer.report;

import io.github.fireres.gui.controller.PresetContainer;
import io.github.fireres.gui.controller.unheated.surface.UnheatedSurface;
import io.github.fireres.gui.initializer.Initializer;
import io.github.fireres.gui.preset.impl.UnheatedSurfacePresetApplier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UnheatedSurfaceInitializer implements Initializer<UnheatedSurface> {

    private final UnheatedSurfacePresetApplier presetApplier;

    @Override
    public void initialize(UnheatedSurface unheatedSurface) {
        presetApplier.apply(unheatedSurface, ((PresetContainer) unheatedSurface.getParent()).getPreset());
    }

}

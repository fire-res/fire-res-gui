package io.github.fireres.gui.unheated.surface.initializer;

import io.github.fireres.gui.framework.controller.PresetContainer;
import io.github.fireres.gui.framework.initializer.Initializer;
import io.github.fireres.gui.unheated.surface.controller.UnheatedSurface;
import io.github.fireres.gui.unheated.surface.preset.UnheatedSurfacePresetApplier;
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

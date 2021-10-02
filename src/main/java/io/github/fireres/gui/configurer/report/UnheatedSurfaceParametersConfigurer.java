package io.github.fireres.gui.configurer.report;

import io.github.fireres.gui.controller.unheated.surface.UnheatedSurface;
import io.github.fireres.gui.preset.Preset;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UnheatedSurfaceParametersConfigurer extends AbstractReportParametersConfigurer<UnheatedSurface> {

    private final UnheatedSurfaceFirstGroupConfigurer firstGroupConfigurer;
    private final UnheatedSurfaceSecondGroupConfigurer secondGroupConfigurer;
    private final UnheatedSurfaceThirdGroupConfigurer thirdGroupConfigurer;

    @Override
    public void config(UnheatedSurface unheatedSurface, Preset preset) {
        firstGroupConfigurer.config(unheatedSurface.getFirstGroup(), preset);
        secondGroupConfigurer.config(unheatedSurface.getSecondGroup(), preset);
        thirdGroupConfigurer.config(unheatedSurface.getThirdGroup(), preset);
    }

}

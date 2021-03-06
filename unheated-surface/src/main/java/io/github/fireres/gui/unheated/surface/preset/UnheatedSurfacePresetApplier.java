package io.github.fireres.gui.unheated.surface.preset;

import io.github.fireres.gui.framework.preset.Preset;
import io.github.fireres.gui.framework.preset.PresetApplier;
import io.github.fireres.gui.unheated.surface.controller.UnheatedSurface;
import io.github.fireres.unheated.surface.properties.UnheatedSurfaceProperties;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Component;

import java.util.List;

import static io.github.fireres.unheated.surface.properties.UnheatedSurfaceType.PRIMARY;
import static io.github.fireres.unheated.surface.properties.UnheatedSurfaceType.SECONDARY;

@Component
@RequiredArgsConstructor
public class UnheatedSurfacePresetApplier implements PresetApplier<UnheatedSurface> {

    private final UnheatedSurfacePrimaryGroupPresetApplier primaryGroupPresetApplier;
    private final UnheatedSurfaceSecondaryGroupPresetApplier secondaryGroupPresetApplier;

    @Override
    public void apply(UnheatedSurface unheatedSurface, Preset preset) {
        getUnheatedSurfaceProperties(preset).forEach(groupProperties -> {
            val groupName = resolveGroupName(unheatedSurface);

            if (groupProperties.getType() == PRIMARY) {
                val groupComponent = unheatedSurface.addPrimaryGroup(groupName, groupProperties.getId());

                primaryGroupPresetApplier.apply(groupComponent, preset);
            } else if (groupProperties.getType() == SECONDARY) {
                val groupComponent = unheatedSurface.addSecondaryGroup(groupName, groupProperties.getId());

                secondaryGroupPresetApplier.apply(groupComponent, preset);
            }
        });
    }

    private String resolveGroupName(UnheatedSurface unheatedSurface) {
        return String.format("Группа №%s", unheatedSurface.getPrimaryGroups().size() + unheatedSurface.getSecondaryGroups().size() + 1);
    }

    private List<UnheatedSurfaceProperties> getUnheatedSurfaceProperties(Preset preset) {
        val presetProperties = preset.findAll(UnheatedSurfaceProperties.class);

        if (!presetProperties.isEmpty()) {
            return presetProperties;
        }

        val defaultProperties = List.of(
                UnheatedSurfaceProperties.builder()
                        .type(PRIMARY)
                        .build(),
                UnheatedSurfaceProperties.builder()
                        .type(SECONDARY)
                        .build(),
                UnheatedSurfaceProperties.builder()
                        .type(SECONDARY)
                        .build()
        );

        preset.getProperties().addAll(defaultProperties);

        return defaultProperties;
    }


}

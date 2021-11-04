package io.github.fireres.gui.framework.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.fireres.gui.framework.preset.Preset;
import io.github.fireres.gui.framework.service.PresetService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PresetServiceImpl implements PresetService {

    @Value("${fire-res.presets.custom-presets-path}")
    private String customPresetsPath;

    private final List<Preset> availablePresets;
    private final ObjectMapper objectMapper;

    @Override
    public List<Preset> getAvailablePresets() {
        return availablePresets;
    }

    @Override
    public Preset getDefaultPreset() {
        return availablePresets.stream()
                .filter(Preset::getApplyingByDefault)
                .findFirst()
                .orElseThrow();
    }

    @Override
    public void savePreset(Preset preset) throws IOException {
        val path = customPresetsPath + "/custom_preset_" + UUID.randomUUID() + ".json";

        try (val bufferedWriter = new BufferedWriter(new FileWriter(path))) {
            bufferedWriter.write(objectMapper.writeValueAsString(preset));
            getAvailablePresets().add(preset);
        }
    }

}

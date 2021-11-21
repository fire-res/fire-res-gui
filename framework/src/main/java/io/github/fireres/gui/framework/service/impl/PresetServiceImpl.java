package io.github.fireres.gui.framework.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.fireres.gui.framework.config.properties.general.PresetsProperties;
import io.github.fireres.gui.framework.preset.Preset;
import io.github.fireres.gui.framework.service.PresetService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PresetServiceImpl implements PresetService {

    private final List<Preset> availablePresets;
    private final ObjectMapper objectMapper;
    private final PresetsProperties presetsProperties;

    @PostConstruct
    public void loadPresets() {
        log.info("Loading presets from: {}", presetsProperties.getPath());
        val presetsDir = new File(presetsProperties.getPath());

        if (!presetsDir.exists()) {
            throw new IllegalArgumentException("Can't load presets, path does not exist: " + presetsDir.getAbsolutePath());
        }

        if (!presetsDir.isDirectory()) {
            throw new IllegalArgumentException("Can't load presets, path is not a directory: " + presetsDir.getAbsolutePath());
        }

        for (File preset : presetsDir.listFiles()) {
            loadPreset(preset);
        }
    }

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
    @SneakyThrows
    public void savePreset(Preset preset) {
        val path = presetsProperties.getPath() + "/custom_preset_" + UUID.randomUUID() + ".json";

        try (val bufferedWriter = new BufferedWriter(new FileWriter(path))) {
            bufferedWriter.write(objectMapper.writeValueAsString(preset));
            getAvailablePresets().add(preset);
        }
    }

    @Override
    public void loadPreset(File preset) {
        log.info("Loading preset: {}", preset.getAbsolutePath());
        if (!preset.exists()) {
            throw new IllegalArgumentException("Can't load presets, file does not exist: " + preset.getAbsolutePath());
        }

        try {
            val loadedPreset = objectMapper.readValue(preset, Preset.class);

            loadedPreset.setFilename(preset.getName());

            availablePresets.add(loadedPreset);
        } catch (IOException e) {
            throw new RuntimeException("Can't load preset: " + preset.getAbsolutePath(), e);
        }

    }

}

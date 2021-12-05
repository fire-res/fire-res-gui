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
import java.nio.charset.StandardCharsets;
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
        val filename = "custom_preset_" + UUID.randomUUID() + ".json";

        savePresetToFile(preset, filename);
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

    @Override
    public void changePresetDescription(Preset preset, String description) {
        preset.setDescription(description);
        updatePresetFile(preset);
    }

    @Override
    public void changePresetFilename(Preset preset, String filename) {
        val currentFile = new File(resolveAbsolutePath(preset.getFilename()));

        if (!currentFile.delete()) {
            throw new RuntimeException("Can't change preset filename: can't delete file " + currentFile);
        }

        savePresetToFile(preset, filename);
    }

    @Override
    public void deletePreset(Preset preset) {
        val fileToDelete = new File(resolveAbsolutePath(preset.getFilename()));

        if (!fileToDelete.delete()) {
            throw new RuntimeException("Can't delete file: " + fileToDelete);
        }

        availablePresets.remove(preset);
    }

    private void updatePresetFile(Preset preset) {
        savePresetToFile(preset, preset.getFilename());
    }

    @SneakyThrows
    private void savePresetToFile(Preset preset, String filename) {
        val path = resolveAbsolutePath(filename);

        try (val bufferedWriter = new BufferedWriter(new FileWriter(path, StandardCharsets.UTF_8))) {
            bufferedWriter.write(objectMapper.writeValueAsString(preset));

            if (!availablePresets.contains(preset)) {
                availablePresets.add(preset);
            }
        }

        preset.setFilename(filename);
    }

    private String resolveAbsolutePath(String filename) {
        return presetsProperties.getPath() + "/" + filename;
    }

}

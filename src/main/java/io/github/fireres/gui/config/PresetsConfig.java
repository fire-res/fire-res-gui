package io.github.fireres.gui.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.fireres.gui.config.properties.general.PresetsProperties;
import io.github.fireres.gui.preset.Preset;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
public class PresetsConfig {

    private final PresetsProperties properties;
    private final ObjectMapper objectMapper;

    @Bean
    public List<Preset> presets() {
        val availablePresets = new ArrayList<Preset>();

        availablePresets.addAll(loadPresets(properties.getDefaultPresetsPath()));
        availablePresets.addAll(loadPresets(properties.getCustomPresetsPath()));

        return availablePresets;
    }

    @SuppressWarnings("ConstantConditions")
    private List<Preset> loadPresets(String path) {
        val presetsDir = new File(path);

        if (!presetsDir.exists()) {
            throw new IllegalArgumentException("Can't load presets, path does not exist: " + path);
        }

        if (!presetsDir.isDirectory()) {
            throw new IllegalArgumentException("Can't load presets, path is not a directory: " + path);
        }

        return Arrays.stream(presetsDir.listFiles())
                .map(presetFile -> {
                    try {
                        return objectMapper.readValue(presetFile, Preset.class);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }

}

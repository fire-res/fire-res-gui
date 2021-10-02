package io.github.fireres.gui.service;

import io.github.fireres.gui.preset.Preset;

import java.io.IOException;
import java.util.List;

public interface PresetService {

    List<Preset> getAvailablePresets();

    Preset getDefaultPreset();

    void savePreset(Preset preset) throws IOException;
}

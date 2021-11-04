package io.github.fireres.gui.framework.service;

import io.github.fireres.gui.framework.preset.Preset;

import java.io.IOException;
import java.util.List;

public interface PresetService {

    List<Preset> getAvailablePresets();

    Preset getDefaultPreset();

    void savePreset(Preset preset) throws IOException;
}

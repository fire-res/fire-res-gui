package io.github.fireres.gui.framework.service;

import io.github.fireres.gui.framework.preset.Preset;

import java.io.File;
import java.util.List;

public interface PresetService {

    List<Preset> getAvailablePresets();

    Preset getDefaultPreset();

    void savePreset(Preset preset);

    void loadPreset(File preset);

}

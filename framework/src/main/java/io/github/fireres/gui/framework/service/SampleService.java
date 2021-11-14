package io.github.fireres.gui.framework.service;

import io.github.fireres.core.model.Sample;
import io.github.fireres.gui.framework.controller.common.SampleTab;
import io.github.fireres.gui.framework.controller.common.SamplesTabPane;
import io.github.fireres.gui.framework.preset.Preset;

public interface SampleService {

    SampleTab createNewSample(SamplesTabPane samplesTabPane);

    void closeSample(SamplesTabPane samplesTabPane, SampleTab closedSampleTab);

    void copySample(SamplesTabPane samplesTabPane, SampleTab sampleTabToCopy);

    Preset mapSampleToPreset(Sample sample, String presetDescription);

}

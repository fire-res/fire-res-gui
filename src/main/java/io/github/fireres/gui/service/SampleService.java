package io.github.fireres.gui.service;

import io.github.fireres.gui.controller.common.SampleTab;
import io.github.fireres.gui.controller.common.SamplesTabPane;

public interface SampleService {

    void createNewSample(SamplesTabPane samplesTabPane);

    void closeSample(SamplesTabPane samplesTabPane, SampleTab closedSampleTab);

}

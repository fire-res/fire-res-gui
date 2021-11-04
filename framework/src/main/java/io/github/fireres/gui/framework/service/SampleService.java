package io.github.fireres.gui.framework.service;

import io.github.fireres.gui.framework.controller.common.SampleTab;
import io.github.fireres.gui.framework.controller.common.SamplesTabPane;

public interface SampleService {

    void createNewSample(SamplesTabPane samplesTabPane);

    void closeSample(SamplesTabPane samplesTabPane, SampleTab closedSampleTab);

}

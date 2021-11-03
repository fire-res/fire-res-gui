package io.github.fireres.gui.initializer.general;

import io.github.fireres.gui.controller.common.SamplesTabPane;
import io.github.fireres.gui.initializer.Initializer;
import io.github.fireres.gui.service.SampleService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SamplesInitializer implements Initializer<SamplesTabPane> {

    private final SampleService sampleService;

    @Override
    public void initialize(SamplesTabPane samplesTabPaneController) {
        val samplesTabPane = samplesTabPaneController.getComponent();

        samplesTabPane.getTabs().removeIf(tab -> !tab.getId().equals("addSampleTab"));
        sampleService.createNewSample(samplesTabPaneController);
    }
}

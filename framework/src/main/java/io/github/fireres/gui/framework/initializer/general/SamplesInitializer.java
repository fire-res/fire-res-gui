package io.github.fireres.gui.framework.initializer.general;

import io.github.fireres.gui.framework.initializer.Initializer;
import io.github.fireres.gui.framework.service.SampleService;
import io.github.fireres.gui.framework.controller.common.SamplesTabPane;
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

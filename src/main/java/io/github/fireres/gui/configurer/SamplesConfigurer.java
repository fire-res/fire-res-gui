package io.github.fireres.gui.configurer;

import io.github.fireres.core.properties.GenerationProperties;
import io.github.fireres.gui.controller.common.SamplesTabPane;
import io.github.fireres.gui.service.SampleService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SamplesConfigurer implements Configurer<SamplesTabPane> {

    private final GenerationProperties generationProperties;
    private final SampleService sampleService;

    @Override
    public void config(SamplesTabPane samplesTabPaneController) {
        val samplesTabPane = samplesTabPaneController.getComponent();

        generationProperties.getSamples().clear();
        samplesTabPane.getTabs().removeIf(tab -> !tab.getId().equals("addSampleTab"));
        sampleService.createNewSample(samplesTabPaneController);
    }
}

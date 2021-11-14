package io.github.fireres.gui.framework.service.impl;

import com.rits.cloning.Cloner;
import io.github.fireres.core.model.Sample;
import io.github.fireres.core.properties.ReportProperties;
import io.github.fireres.core.properties.SampleProperties;
import io.github.fireres.gui.framework.controller.ReportTab;
import io.github.fireres.gui.framework.controller.common.SampleTab;
import io.github.fireres.gui.framework.controller.common.SamplesTabPane;
import io.github.fireres.gui.framework.preset.Preset;
import io.github.fireres.gui.framework.service.FxmlLoadService;
import io.github.fireres.gui.framework.service.SampleService;
import javafx.scene.control.TabPane;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SampleServiceImpl implements SampleService {

    public static final String SAMPLE_NAME_TEMPLATE = "Образец №%d";

    private final AtomicInteger sampleCounter = new AtomicInteger(0);

    private final FxmlLoadService fxmlLoadService;
    private final Cloner cloner;

    @Override
    public SampleTab createNewSample(SamplesTabPane samplesTabPane) {
        val samplesTabPaneComponent = samplesTabPane.getComponent();
        val sampleName = createSampleName(samplesTabPane);

        val newSampleProperties = new SampleProperties();

        newSampleProperties.setName(sampleName);

        val newTab = createSampleTab(samplesTabPane, newSampleProperties, sampleName);

        samplesTabPaneComponent.getTabs().add(samplesTabPaneComponent.getTabs().size() - 1, newTab.getComponent());
        samplesTabPaneComponent.getSelectionModel().select(newTab.getComponent());

        return newTab;
    }

    @Override
    public void closeSample(SamplesTabPane samplesTabPane, SampleTab closedSampleTab) {
        samplesTabPane.getChildren().removeIf(tab -> tab.equals(closedSampleTab));

        if (samplesTabPane.getComponent().getTabs().size() == 2) {
            samplesTabPane.getComponent().setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        }
    }

    @Override
    public void copySample(SamplesTabPane samplesTabPane, SampleTab sampleTabToCopy) {
        val newSampleTab = createNewSample(samplesTabPane);

        mergeSampleTabs(newSampleTab, sampleTabToCopy);
    }

    @Override
    public Preset mapSampleToPreset(Sample sample, String presetDescription) {
        return Preset.builder()
                .applyingByDefault(false)
                .description(presetDescription)
                .properties(copyReportProperties(sample))
                .build();
    }

    private void mergeSampleTabs(SampleTab target, SampleTab source) {
        target.setPreset(source.getPreset());

        val presetToApply = mapSampleToPreset(source.getSample(), null);

        target.getChildren(ReportTab.class).forEach(reportTab -> reportTab.changePreset(presetToApply));
    }

    private List<ReportProperties> copyReportProperties(Sample sample) {
        return sample.getReportProperties().stream()
                .map(props -> {
                    val copy = cloner.deepClone(props);

                    copy.setId(UUID.randomUUID());

                    return copy;
                })
                .collect(Collectors.toList());
    }

    @SneakyThrows
    private SampleTab createSampleTab(SamplesTabPane samplesTabPane,
                                      SampleProperties sampleProperties,
                                      String tabName) {

        val sample = (SampleTab) fxmlLoadService.loadComponent(
                SampleTab.class,
                samplesTabPane,
                sampleTab -> sampleTab.setSample(new Sample(sampleProperties)));

        sample.getComponent().setText(tabName);

        return sample;
    }

    private String createSampleName(SamplesTabPane samplesTabPane) {
        var name = String.format(SAMPLE_NAME_TEMPLATE, sampleCounter.incrementAndGet());
        val sampleNames = samplesTabPane.getSampleTabs().stream()
                .map(tab -> tab.getSample().getSampleProperties().getName())
                .collect(Collectors.toList());

        while (sampleNameAlreadyExists(name, sampleNames)) {
            name = String.format(SAMPLE_NAME_TEMPLATE, sampleCounter.incrementAndGet());
        }

        return name;
    }

    private boolean sampleNameAlreadyExists(String name, List<String> samplesNames) {
        return samplesNames.stream().anyMatch(name::equals);
    }

}

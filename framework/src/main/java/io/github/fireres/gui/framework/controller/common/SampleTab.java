package io.github.fireres.gui.framework.controller.common;

import io.github.fireres.core.model.ReportType;
import io.github.fireres.gui.framework.annotation.ContextMenu;
import io.github.fireres.gui.framework.annotation.FxmlView;
import io.github.fireres.gui.framework.annotation.Initialize;
import io.github.fireres.gui.framework.controller.AbstractComponent;
import io.github.fireres.gui.framework.controller.PresetChanger;
import io.github.fireres.gui.framework.controller.PresetContainer;
import io.github.fireres.gui.framework.controller.Refreshable;
import io.github.fireres.gui.framework.controller.ReportTab;
import io.github.fireres.gui.framework.controller.SampleContainer;
import io.github.fireres.gui.framework.controller.modal.SamplePresetChangeModalWindow;
import io.github.fireres.gui.framework.controller.modal.SampleRenameModalWindow;
import io.github.fireres.gui.framework.controller.modal.SampleSavePresetModalWindow;
import io.github.fireres.core.model.Sample;
import io.github.fireres.gui.framework.initializer.general.SampleTabInitializer;
import io.github.fireres.gui.framework.preset.Preset;
import io.github.fireres.gui.framework.service.FxmlLoadService;
import io.github.fireres.gui.framework.service.PresetService;
import io.github.fireres.gui.framework.service.SampleService;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@Slf4j
@FxmlView("sample.fxml")
@RequiredArgsConstructor
@Component
@Scope(scopeName = SCOPE_PROTOTYPE)
@ContextMenu({
        @ContextMenu.Item(text = "Переименовать", handler = "handleRenamePressed"),
        @ContextMenu.Item(text = "Изменить пресет", handler = "handleChangePresetPressed"),
        @ContextMenu.Item(text = "Сохранить как пресет", handler = "handleSavePressed"),
        @ContextMenu.Item(text = "Копировать", handler = "handleCopyPressed")
})
@Initialize(SampleTabInitializer.class)
public class SampleTab extends AbstractComponent<Tab>
        implements SampleContainer, PresetContainer, PresetChanger, Refreshable {

    @FXML
    @Getter
    public TabPane reportsTabPane;

    @Setter
    @Getter
    private Sample sample;

    @Setter
    @Getter
    private Preset preset;

    private final SampleService sampleService;
    private final FxmlLoadService fxmlLoadService;
    private final PresetService presetService;

    @Override
    public void changePreset(Preset preset) {
        this.preset = preset;

        getChildren(ReportTab.class)
                .forEach(reportTab -> reportTab.changePreset(preset));
    }

    @FXML
    public void closeSample() {
        log.info("Close sample button pressed");

        val samplesTabPane = (SamplesTabPane) getParent();

        sampleService.closeSample(samplesTabPane, this);
    }

    @Override
    public void refresh() {
        getChildren(ReportTab.class).forEach(Refreshable::refresh);
    }

    @ContextMenu.Handler
    public void handleChangePresetPressed() {
        fxmlLoadService.loadComponent(SamplePresetChangeModalWindow.class, this).getWindow().show();
    }

    @ContextMenu.Handler
    public void handleRenamePressed() {
        fxmlLoadService.loadComponent(SampleRenameModalWindow.class, this).getWindow().show();
    }

    @ContextMenu.Handler
    public void handleSavePressed() {
        fxmlLoadService.loadComponent(SampleSavePresetModalWindow.class, this).getWindow().show();
    }

    @ContextMenu.Handler
    public void handleCopyPressed() {
        sampleService.copySample((SamplesTabPane) getParent(), this);
    }

    public ReportTab getReportTabByType(ReportType reportType) {
        return getChildren(ReportTab.class).stream()
                .filter(reportTab -> reportTab.getReportType().equals(reportType))
                .findFirst()
                .orElseThrow();
    }

    @Override
    public Preset getPreset() {
        if (this.preset == null) {
            this.preset = presetService.getDefaultPreset();
        }

        return preset;
    }
}



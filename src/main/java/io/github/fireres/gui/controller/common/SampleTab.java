package io.github.fireres.gui.controller.common;

import io.github.fireres.core.model.Sample;
import io.github.fireres.gui.annotation.ContextMenu;
import io.github.fireres.gui.annotation.ContextMenu.Item;
import io.github.fireres.gui.annotation.FxmlView;
import io.github.fireres.gui.controller.AbstractComponent;
import io.github.fireres.gui.controller.PresetChanger;
import io.github.fireres.gui.controller.PresetContainer;
import io.github.fireres.gui.controller.Refreshable;
import io.github.fireres.gui.controller.SampleContainer;
import io.github.fireres.gui.controller.excess.pressure.ExcessPressure;
import io.github.fireres.gui.controller.fire.mode.FireMode;
import io.github.fireres.gui.controller.heat.flow.HeatFlow;
import io.github.fireres.gui.controller.modal.SamplePresetChangeModalWindow;
import io.github.fireres.gui.controller.modal.SampleRenameModalWindow;
import io.github.fireres.gui.controller.modal.SampleSavePresetModalWindow;
import io.github.fireres.gui.controller.unheated.surface.UnheatedSurface;
import io.github.fireres.gui.preset.Preset;
import io.github.fireres.gui.service.FxmlLoadService;
import io.github.fireres.gui.service.PresetService;
import io.github.fireres.gui.service.SampleService;
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
        @Item(text = "Переименовать", handler = "handleRenamePressed"),
        @Item(text = "Изменить пресет", handler = "handleChangePresetPressed"),
        @Item(text = "Сохранить как пресет", handler = "handleSavePressed")
})
public class SampleTab extends AbstractComponent<Tab>
        implements SampleContainer, PresetContainer, PresetChanger, Refreshable {

    @FXML
    @Getter
    public TabPane reportsTabPane;

    @FXML
    @Getter
    private Tab unheatedSurfaceTab;

    @FXML
    @Getter
    private Tab heatFlowTab;

    @FXML
    @Getter
    private Tab excessPressureTab;

    @FXML
    @Getter
    private Tab fireModeTab;

    @Setter
    private Sample sample;

    @FXML
    private ExcessPressure excessPressureController;

    @FXML
    private FireMode fireModeController;

    @FXML
    private UnheatedSurface unheatedSurfaceController;

    @FXML
    private HeatFlow heatFlowController;

    private Preset preset;

    private final SampleService sampleService;
    private final FxmlLoadService fxmlLoadService;
    private final PresetService presetService;

    @Override
    public void changePreset(Preset preset) {
        this.preset = preset;

        getExcessPressure().changePreset(preset);
        getFireMode().changePreset(preset);
        getHeatFlow().changePreset(preset);
        getUnheatedSurface().changePreset(preset);
    }

    @FXML
    public void closeSample() {
        log.info("Close sample button pressed");

        val samplesTabPane = (SamplesTabPane) getParent();

        sampleService.closeSample(samplesTabPane, this);
    }

    @Override
    public Sample getSample() {
        return sample;
    }

    @Override
    public void refresh() {
        getFireMode().refresh();
        getExcessPressure().refresh();
        getHeatFlow().refresh();
        getUnheatedSurface().refresh();
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

    public FireMode getFireMode() {
        return fireModeController;
    }

    public ExcessPressure getExcessPressure() {
        return excessPressureController;
    }

    public HeatFlow getHeatFlow() {
        return heatFlowController;
    }

    public UnheatedSurface getUnheatedSurface() {
        return unheatedSurfaceController;
    }

    @Override
    public Preset getPreset() {
        if (this.preset == null) {
            this.preset = presetService.getDefaultPreset();
        }

        return preset;
    }
}



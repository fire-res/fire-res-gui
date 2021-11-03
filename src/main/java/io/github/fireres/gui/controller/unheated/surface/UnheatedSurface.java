package io.github.fireres.gui.controller.unheated.surface;

import io.github.fireres.core.model.Sample;
import io.github.fireres.core.properties.GeneralProperties;
import io.github.fireres.gui.annotation.Initialize;
import io.github.fireres.gui.controller.AbstractComponent;
import io.github.fireres.gui.controller.PresetChanger;
import io.github.fireres.gui.controller.Refreshable;
import io.github.fireres.gui.controller.ReportInclusionChanger;
import io.github.fireres.gui.controller.SampleContainer;
import io.github.fireres.gui.controller.common.SampleTab;
import io.github.fireres.gui.controller.unheated.surface.groups.primary.PrimaryGroup;
import io.github.fireres.gui.controller.unheated.surface.groups.secondary.SecondaryGroup;
import io.github.fireres.gui.initializer.report.UnheatedSurfaceInitializer;
import io.github.fireres.gui.preset.Preset;
import io.github.fireres.gui.preset.impl.UnheatedSurfacePresetApplier;
import io.github.fireres.gui.service.FxmlLoadService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

import static io.github.fireres.core.properties.ReportType.UNHEATED_SURFACE;
import static io.github.fireres.gui.util.TabUtils.disableTab;
import static io.github.fireres.gui.util.TabUtils.enableTab;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@RequiredArgsConstructor
@Component
@Scope(scopeName = SCOPE_PROTOTYPE)
@Initialize(UnheatedSurfaceInitializer.class)
public class UnheatedSurface extends AbstractComponent<ScrollPane>
        implements ReportInclusionChanger, PresetChanger, Refreshable, SampleContainer {

    @FXML
    public VBox groups;

    private final GeneralProperties generationProperties;
    private final FxmlLoadService fxmlLoadService;
    private final UnheatedSurfacePresetApplier presetApplier;

    @Override
    public void postConstruct() {
        excludeReportIfNeeded();
    }

    private void excludeReportIfNeeded() {
        if (isReportExcluded()) {
            excludeReport();
        }
    }

    private boolean isReportExcluded() {
        return !generationProperties.getIncludedReports().contains(UNHEATED_SURFACE);
    }

    @Override
    public void excludeReport() {
        getPrimaryGroups().forEach(group -> getSample().removeReport(group.getReport()));
        getSecondaryGroups().forEach(group -> getSample().removeReport(group.getReport()));
        disableTab(((SampleTab) getParent()).getUnheatedSurfaceTab());
        generationProperties.getIncludedReports().removeIf(UNHEATED_SURFACE::equals);
    }

    @Override
    public void includeReport() {
        val parent = ((SampleTab) getParent());

        getPrimaryGroups().forEach(group -> getSample().addReport(group.getReport()));
        getSecondaryGroups().forEach(group -> getSample().addReport(group.getReport()));
        enableTab(parent.getUnheatedSurfaceTab(), parent.getReportsTabPane());
        generationProperties.getIncludedReports().add(UNHEATED_SURFACE);
    }

    @Override
    public void changePreset(Preset preset) {
        clearGroups();
        Platform.runLater(() -> presetApplier.apply(this, preset));
    }

    @Override
    public void refresh() {
        if (!isReportExcluded()) {
            getPrimaryGroups().forEach(PrimaryGroup::refresh);
            getSecondaryGroups().forEach(SecondaryGroup::refresh);
        }
    }

    public List<PrimaryGroup> getPrimaryGroups() {
        return getChildren(PrimaryGroup.class);
    }

    public List<SecondaryGroup> getSecondaryGroups() {
        return getChildren(SecondaryGroup.class);
    }

    public PrimaryGroup addPrimaryGroup(String groupName, UUID propertiesId) {
        val group = fxmlLoadService.loadComponent(PrimaryGroup.class, this,
                g -> g.setPropertiesId(propertiesId));

        group.getComponent().setText(groupName);
        groups.getChildren().add(group.getComponent());

        return group;
    }

    public SecondaryGroup addSecondaryGroup(String groupName, UUID propertiesId) {
        val group = fxmlLoadService.loadComponent(SecondaryGroup.class, this,
                g -> g.setPropertiesId(propertiesId));

        group.getComponent().setText(groupName);
        groups.getChildren().add(group.getComponent());

        return group;
    }

    private void clearGroups() {
        groups.getChildren().clear();
        removeChildren(PrimaryGroup.class);
        removeChildren(SecondaryGroup.class);
    }

    @Override
    public Sample getSample() {
        return ((SampleTab) getParent()).getSample();
    }

}

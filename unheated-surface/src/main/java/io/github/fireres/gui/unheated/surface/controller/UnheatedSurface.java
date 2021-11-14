package io.github.fireres.gui.unheated.surface.controller;

import io.github.fireres.core.model.ReportType;
import io.github.fireres.core.model.Sample;
import io.github.fireres.core.properties.GeneralProperties;
import io.github.fireres.gui.framework.annotation.FxmlView;
import io.github.fireres.gui.framework.annotation.Initialize;
import io.github.fireres.gui.framework.controller.ChartContainer;
import io.github.fireres.gui.framework.controller.ExtendedComponent;
import io.github.fireres.gui.framework.controller.ReportTab;
import io.github.fireres.gui.framework.controller.SampleContainer;
import io.github.fireres.gui.framework.controller.common.SampleTab;
import io.github.fireres.gui.framework.preset.Preset;
import io.github.fireres.gui.framework.service.FxmlLoadService;
import io.github.fireres.gui.framework.service.ReportTabService;
import io.github.fireres.gui.unheated.surface.controller.groups.primary.PrimaryGroup;
import io.github.fireres.gui.unheated.surface.controller.groups.secondary.SecondaryGroup;
import io.github.fireres.gui.unheated.surface.initializer.UnheatedSurfaceInitializer;
import io.github.fireres.gui.unheated.surface.preset.UnheatedSurfacePresetApplier;
import io.github.fireres.unheated.surface.report.UnheatedSurfaceReport;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

import static io.github.fireres.unheated.surface.report.UnheatedSurfaceReportType.UNHEATED_SURFACE;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@FxmlView("unheatedSurface.fxml")
@RequiredArgsConstructor
@Component
@Scope(scopeName = SCOPE_PROTOTYPE)
@Initialize(UnheatedSurfaceInitializer.class)
public class UnheatedSurface extends ReportTab implements SampleContainer {

    @FXML
    public VBox groups;

    private final GeneralProperties generationProperties;
    private final FxmlLoadService fxmlLoadService;
    private final UnheatedSurfacePresetApplier presetApplier;
    private final ReportTabService tabService;

    @Override
    public void excludeReport() {
        tabService.disableTab(getComponent());
        generationProperties.getIncludedReports().removeIf(UNHEATED_SURFACE::equals);
    }

    @Override
    public void includeReport() {
        val parent = ((SampleTab) getParent());

        tabService.enableTab(getComponent(), parent.getReportsTabPane());
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
        getSample().getReports().removeIf(report -> report instanceof UnheatedSurfaceReport);
        removeChildren(PrimaryGroup.class);
        removeChildren(SecondaryGroup.class);
    }

    public void collapseGroupsExcept(ExtendedComponent<?> group) {
        getPrimaryGroups().stream()
                .filter(g -> g != group)
                .forEach(g -> g.getComponent().setExpanded(false));

        getSecondaryGroups().stream()
                .filter(g -> g != group)
                .forEach(g -> g.getComponent().setExpanded(false));
    }

    @Override
    public Sample getSample() {
        return ((SampleTab) getParent()).getSample();
    }

    @Override
    public ChartContainer getChartContainer() {
        throw new UnsupportedOperationException();
    }

    @Override
    public UUID getReportId() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ReportType getReportType() {
        return UNHEATED_SURFACE;
    }
}

package io.github.fireres.gui.unheated.surface.controller.groups.primary;

import io.github.fireres.core.model.Sample;
import io.github.fireres.gui.framework.annotation.FxmlView;
import io.github.fireres.gui.framework.controller.AbstractReportUpdaterComponent;
import io.github.fireres.gui.framework.controller.ChartContainer;
import io.github.fireres.gui.framework.controller.ReportUpdater;
import io.github.fireres.gui.unheated.surface.controller.UnheatedSurfaceReportContainer;
import io.github.fireres.unheated.surface.report.UnheatedSurfaceReport;
import io.github.fireres.unheated.surface.service.UnheatedSurfaceService;
import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import javafx.scene.control.TitledPane;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@FxmlView("primaryGroupParams.fxml")
@RequiredArgsConstructor
@Component
@Scope(scopeName = SCOPE_PROTOTYPE)
public class PrimaryGroupParams extends AbstractReportUpdaterComponent<TitledPane>
        implements UnheatedSurfaceReportContainer {

    @FXML
    @Getter
    private Spinner<Integer> thermocouples;

    private final UnheatedSurfaceService unheatedSurfaceService;

    @FXML
    public void handleThermocouplesCountChanged() {
        updateReport(
                () -> unheatedSurfaceService.updateThermocoupleCount(getReport(), thermocouples.getValue()),
                ((PrimaryGroup) getParent()).getParamsVbox());
    }

    @Override
    public Sample getSample() {
        return ((PrimaryGroup) getParent()).getSample();
    }

    @Override
    public UnheatedSurfaceReport getReport() {
        return ((PrimaryGroup) getParent()).getReport();
    }

    @Override
    public ChartContainer getChartContainer() {
        return ((PrimaryGroup) getParent()).getChartContainer();
    }

    @Override
    public UUID getReportId() {
        return ((ReportUpdater) getParent()).getReportId();
    }

}

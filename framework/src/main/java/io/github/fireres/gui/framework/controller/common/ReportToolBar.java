package io.github.fireres.gui.framework.controller.common;

import io.github.fireres.gui.framework.controller.AbstractComponent;
import io.github.fireres.gui.framework.controller.ChartContainer;
import io.github.fireres.gui.framework.controller.Refreshable;
import io.github.fireres.gui.framework.controller.ReportContainer;
import io.github.fireres.gui.framework.controller.ReportDataCollector;
import io.github.fireres.gui.framework.controller.ReportUpdater;
import io.github.fireres.gui.framework.controller.Resettable;
import io.github.fireres.gui.framework.controller.SampleContainer;
import io.github.fireres.gui.framework.controller.modal.DataViewerModalWindow;
import io.github.fireres.core.model.Report;
import io.github.fireres.core.model.Sample;
import io.github.fireres.gui.framework.service.AlertService;
import io.github.fireres.gui.framework.service.FxmlLoadService;
import io.github.fireres.gui.framework.service.ReportExecutorService;
import io.github.fireres.gui.framework.service.ReportUpdateListener;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToolBar;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@SuppressWarnings("rawtypes")
@RequiredArgsConstructor
@Component
@Scope(scopeName = SCOPE_PROTOTYPE)
public class ReportToolBar extends AbstractComponent<ToolBar>
        implements SampleContainer, ReportContainer {

    @FXML
    private ToggleButton legendVisibilityButton;

    private final AlertService alertService;
    private final FxmlLoadService fxmlLoadService;
    private final ReportExecutorService reportExecutorService;

    @FXML
    public void refreshReport() {
        ((Refreshable) getParent()).refresh();
    }

    @FXML
    public void resetSettings() {
        alertService.showConfirmation(
                "Вы уверены, что хотите сбросить все параметры?",
                () -> ((Resettable) getParent()).reset());
    }

    @FXML
    public void changeLegendVisibility() {
        if (!legendVisibilityButton.isSelected()) {
            legendVisibilityButton.getTooltip().setText("Показать легенду");
            getChartContainer().getChart().setLegendVisible(false);
        } else {
            legendVisibilityButton.getTooltip().setText("Скрыть легенду");
            getChartContainer().getChart().setLegendVisible(true);
        }
    }

    @FXML
    public void showReportData() {
        val modalWindow = fxmlLoadService.loadComponent(DataViewerModalWindow.class, this);

        updateDataViewer(modalWindow);
        modalWindow.getWindow().show();

        val listener = new ReportUpdateListener() {
            @Override
            public void postUpdate(UUID elementId) {
                if (elementId.equals(((ReportUpdater) getParent()).getReportId())) {
                    Platform.runLater(() -> updateDataViewer(modalWindow));
                }
            }
        };

        reportExecutorService.addListener(listener);
        modalWindow.getWindow().setOnCloseRequest(event -> reportExecutorService.removeListener(listener));
    }

    private void updateDataViewer(DataViewerModalWindow modalWindow) {
        val dataViewer = ((ReportDataCollector) getParent()).getReportData();

        modalWindow.setDataViewer(dataViewer);
    }

    @Override
    public Report getReport() {
        return ((ReportContainer) getParent()).getReport();
    }

    @Override
    public ChartContainer getChartContainer() {
        return ((ReportContainer) getParent()).getChartContainer();
    }

    @Override
    public Sample getSample() {
        return ((ReportContainer) getParent()).getSample();
    }

}

package io.github.fireres.gui.framework.controller.modal;

import io.github.fireres.core.model.Report;
import io.github.fireres.core.model.Sample;
import io.github.fireres.gui.framework.annotation.FxmlView;
import io.github.fireres.gui.framework.annotation.ModalWindow;
import io.github.fireres.gui.framework.component.FireResSpinner;
import io.github.fireres.gui.framework.controller.AbstractReportUpdaterComponent;
import io.github.fireres.gui.framework.controller.ChartContainer;
import io.github.fireres.gui.framework.controller.ReportContainer;
import io.github.fireres.gui.framework.controller.ReportUpdater;
import io.github.fireres.gui.framework.controller.common.BoundShift;
import io.github.fireres.gui.framework.exception.NotNotifiableException;
import io.github.fireres.gui.framework.service.AlertService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@SuppressWarnings("rawtypes")
@FxmlView("boundsShiftModalWindow.fxml")
@ModalWindow(title = "Смещение границы")
@Slf4j
@RequiredArgsConstructor
@Component
@Scope(scopeName = SCOPE_PROTOTYPE)
public class BoundShiftModalWindow extends AbstractReportUpdaterComponent<Pane>
        implements ReportContainer {

    @FXML
    private FireResSpinner<Integer> time;

    @FXML
    private FireResSpinner<Number> value;

    @ModalWindow.Window
    @Getter
    private Stage window;

    private final AlertService alertService;

    @FXML
    public void addShift() {
        val parent = ((BoundShift) getParent());

        updateReport(() -> {
            val newPoint = parent.getShiftPointConstructor().apply(time.getValue(), value.getValue());

            try {
                parent.getShiftAddedConsumer().accept(newPoint);
            } catch (Exception e) {
                Platform.runLater(() -> alertService.showError("Невозможно сгенерировать график с данным смещением"));
                throw new NotNotifiableException(e);
            }

            Platform.runLater(() -> parent.getBoundShiftTable().getItems().add(newPoint));
        }, parent.getNodesToBlockOnUpdate());
    }

    @FXML
    public void closeWindow() {
        window.close();
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
    public UUID getReportId() {
        return ((ReportUpdater) getParent()).getReportId();
    }

    @Override
    public Sample getSample() {
        return ((ReportContainer) getParent()).getSample();
    }
}

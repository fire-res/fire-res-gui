package io.github.fireres.gui.controller.modal;

import io.github.fireres.core.model.Report;
import io.github.fireres.core.model.Sample;
import io.github.fireres.gui.annotation.ModalWindow;
import io.github.fireres.gui.component.ContextlessSpinner;
import io.github.fireres.gui.controller.AbstractReportUpdaterComponent;
import io.github.fireres.gui.controller.ChartContainer;
import io.github.fireres.gui.controller.ReportContainer;
import io.github.fireres.gui.controller.ReportUpdater;
import io.github.fireres.gui.controller.common.BoundShift;
import io.github.fireres.gui.exception.NotNotifiableException;
import io.github.fireres.gui.service.AlertService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import io.github.fireres.gui.annotation.FxmlView;
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
    private ContextlessSpinner<Integer> time;

    @FXML
    private ContextlessSpinner<Number> value;

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
    public UUID getUpdatingElementId() {
        return ((ReportUpdater) getParent()).getUpdatingElementId();
    }

    @Override
    public Sample getSample() {
        return ((ReportContainer) getParent()).getSample();
    }
}

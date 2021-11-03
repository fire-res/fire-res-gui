package io.github.fireres.gui.controller.modal;

import io.github.fireres.core.model.Report;
import io.github.fireres.core.model.Sample;
import io.github.fireres.gui.annotation.FxmlView;
import io.github.fireres.gui.annotation.ModalWindow;
import io.github.fireres.gui.controller.AbstractReportUpdaterComponent;
import io.github.fireres.gui.controller.ChartContainer;
import io.github.fireres.gui.controller.ReportContainer;
import io.github.fireres.gui.controller.ReportUpdater;
import io.github.fireres.gui.controller.common.FunctionParams;
import io.github.fireres.gui.exception.NotNotifiableException;
import io.github.fireres.gui.service.AlertService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
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

@SuppressWarnings("unchecked")
@FxmlView("interpolationPointsModalWindow.fxml")
@ModalWindow(title = "Добавление точек интерполяции")
@Slf4j
@RequiredArgsConstructor
@Component
@Scope(scopeName = SCOPE_PROTOTYPE)
public class InterpolationPointsModalWindow extends AbstractReportUpdaterComponent<Pane>
        implements ReportContainer {

    @FXML
    @Getter
    private Spinner<Integer> time;

    @FXML
    @Getter
    private Spinner<Number> value;

    @ModalWindow.Window
    @Getter
    private Stage window;

    private final AlertService alertService;

    @FXML
    public void addInterpolationPoint() {
        val parent = ((FunctionParams) getParent());

        updateReport(() -> {
            val newPoint = parent.getInterpolationPointConstructor()
                    .apply(time.getValue(), value.getValue());

            try {
                parent.getInterpolationService().addInterpolationPoint(getReport(), newPoint);
            } catch (Exception e) {
                Platform.runLater(() -> alertService.showError("Невозможно сгенерировать график с данной точкой"));
                throw new NotNotifiableException(e);
            }

            Platform.runLater(() -> parent.getInterpolationPoints().getItems().add(newPoint));
        }, parent.getNodesToBlockOnUpdate());
    }

    @FXML
    public void closeWindow() {
        window.close();
    }

    @Override
    public Report getReport() {
        return ((FunctionParams) getParent()).getReport();
    }

    @Override
    public ChartContainer getChartContainer() {
        return ((FunctionParams) getParent()).getChartContainer();
    }

    @Override
    public UUID getReportId() {
        return ((ReportUpdater) getParent()).getReportId();
    }

    @Override
    public Sample getSample() {
        return ((FunctionParams) getParent()).getSample();
    }

}

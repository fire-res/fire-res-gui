package io.github.fireres.gui.framework.controller.common;

import io.github.fireres.gui.framework.annotation.ColumnProperty;
import io.github.fireres.gui.framework.annotation.ContextMenu;
import io.github.fireres.gui.framework.annotation.FxmlView;
import io.github.fireres.gui.framework.annotation.TableContextMenu;
import io.github.fireres.gui.framework.controller.AbstractReportUpdaterComponent;
import io.github.fireres.gui.framework.controller.ChartContainer;
import io.github.fireres.gui.framework.controller.ReportContainer;
import io.github.fireres.gui.framework.controller.ReportUpdater;
import io.github.fireres.gui.framework.controller.SampleContainer;
import io.github.fireres.gui.framework.controller.modal.InterpolationPointsModalWindow;
import io.github.fireres.core.model.Point;
import io.github.fireres.core.model.Report;
import io.github.fireres.core.model.Sample;
import io.github.fireres.gui.framework.service.FxmlLoadService;
import io.github.fireres.core.service.InterpolationService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.function.BiFunction;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@SuppressWarnings({"unchecked", "rawtypes"})
@Component
@Scope(scopeName = SCOPE_PROTOTYPE)
@FxmlView("functionParams.fxml")
@RequiredArgsConstructor
public class FunctionParams extends AbstractReportUpdaterComponent<TitledPane>
        implements SampleContainer, ReportContainer {

    @FXML
    @Getter
    private Spinner<Double> childFunctionsDeltaCoefficient;


    @FXML
    @Getter
    private Spinner<Double> linearityCoefficient;

    @FXML
    @Getter
    private Spinner<Double> dispersionCoefficient;

    @FXML
    @Getter
    @TableContextMenu(
            table = @ContextMenu({
                    @ContextMenu.Item(text = "Добавить", handler = "handlePointAddPressed"),
            }),
            row = @ContextMenu({
                    @ContextMenu.Item(text = "Добавить", handler = "handlePointAddPressedOnRow"),
                    @ContextMenu.Item(text = "Удалить", handler = "handlePointDeletePressed")
            })
    )
    private TableView<Point<?>> interpolationPoints;

    @FXML
    @Getter
    @ColumnProperty("time")
    private TableColumn<Point<?>, Integer> timeColumn;

    @FXML
    @Getter
    @ColumnProperty("value")
    private TableColumn<Point<?>, Integer> valueColumn;

    @Getter
    @Setter
    private InterpolationService interpolationService;

    @Getter
    @Setter
    private BiFunction<Integer, Number, Point<?>> interpolationPointConstructor;

    @Getter
    @Setter
    private List<Node> nodesToBlockOnUpdate;

    private final FxmlLoadService fxmlLoadService;

    @ContextMenu.Handler
    public void handlePointDeletePressed(TableRow<Point<?>> affectedRow) {
        updateReport(() -> {
            interpolationService.removeInterpolationPoint(getReport(), affectedRow.getItem());
            Platform.runLater(() -> interpolationPoints.getItems().remove(affectedRow.getItem()));
        }, nodesToBlockOnUpdate);
    }

    @ContextMenu.Handler
    public void handlePointAddPressedOnRow(TableRow<Point<?>> affectedRow) {
        handlePointAddPressed();
    }

    @ContextMenu.Handler
    public void handlePointAddPressed() {
        fxmlLoadService.loadComponent(InterpolationPointsModalWindow.class, this).getWindow().show();
    }

    @FXML
    public void handleLinearityCoefficientChanged() {
        updateReport(
                () -> interpolationService.updateLinearityCoefficient(getReport(), linearityCoefficient.getValue()),
                nodesToBlockOnUpdate);
    }

    @FXML
    public void handleDispersionCoefficientChanged() {
        updateReport(
                () -> interpolationService.updateDispersionCoefficient(getReport(), dispersionCoefficient.getValue()),
                nodesToBlockOnUpdate);
    }

    @FXML
    public void handleChildFunctionsDeltaCoefficientChanged() {
        updateReport(
                () -> interpolationService.updateChildFunctionsDeltaCoefficient(getReport(), childFunctionsDeltaCoefficient.getValue()),
                nodesToBlockOnUpdate);
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

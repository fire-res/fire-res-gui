package io.github.fireres.gui.controller.common;

import io.github.fireres.core.model.Point;
import io.github.fireres.core.model.Report;
import io.github.fireres.core.model.Sample;
import io.github.fireres.core.properties.FunctionForm;
import io.github.fireres.core.properties.SampleProperties;
import io.github.fireres.core.service.InterpolationService;
import io.github.fireres.gui.controller.AbstractReportUpdaterComponent;
import io.github.fireres.gui.controller.ChartContainer;
import io.github.fireres.gui.controller.ReportContainer;
import io.github.fireres.gui.controller.ReportUpdater;
import io.github.fireres.gui.controller.SampleContainer;
import io.github.fireres.gui.controller.modal.InterpolationPointsModalWindow;
import io.github.fireres.gui.service.FxmlLoadService;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.val;
import io.github.fireres.gui.annotation.FxmlView;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;

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
    private TableView<Point<?>> interpolationPoints;

    @FXML
    @Getter
    private TableColumn<Point<?>, Integer> timeColumn;

    @FXML
    @Getter
    private TableColumn<Point<?>, Integer> valueColumn;

    @Getter
    @Setter
    private InterpolationService interpolationService;

    @Getter
    @Setter
    private Function<SampleProperties, FunctionForm> propertiesMapper;

    @Getter
    @Setter
    private BiFunction<Integer, Number, Point<?>> interpolationPointConstructor;

    @Getter
    @Setter
    private List<Node> nodesToBlockOnUpdate;

    private final FxmlLoadService fxmlLoadService;

    @Override
    public void postConstruct() {
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));

        initializeTableContextMenu();
        initializeRowContextMenu();
    }

    private void initializeTableContextMenu() {
        val contextMenu = createTableContextMenu();

        interpolationPoints.setContextMenu(contextMenu);
    }

    private ContextMenu createTableContextMenu() {
        val contextMenu = new ContextMenu();
        val addPointMenuItem = new MenuItem("Добавить");

        addPointMenuItem.setOnAction(this::handleRowAddedEvent);
        contextMenu.getItems().add(addPointMenuItem);

        return contextMenu;
    }

    private void initializeRowContextMenu() {
        interpolationPoints.setRowFactory(
                tableView -> {
                    val row = new TableRow<Point<?>>();
                    val contextMenu = createRowContextMenu(row);

                    row.contextMenuProperty().bind(
                            Bindings.when(row.emptyProperty().not())
                                    .then(contextMenu)
                                    .otherwise((ContextMenu) null));

                    return row;
                });
    }

    private ContextMenu createRowContextMenu(TableRow<Point<?>> row) {
        val rowMenu = new ContextMenu();
        val addPointMenuItem = new MenuItem("Добавить");
        val removePointMenuItem = new MenuItem("Удалить");

        addPointMenuItem.setOnAction(this::handleRowAddedEvent);
        removePointMenuItem.setOnAction(event -> handleRowDeletedEvent(row));
        rowMenu.getItems().addAll(addPointMenuItem, removePointMenuItem);

        return rowMenu;
    }

    private void handleRowDeletedEvent(TableRow<Point<?>> affectedRow) {
        Runnable action = () -> {
            interpolationService.removeInterpolationPoint(getReport(), affectedRow.getItem());
            Platform.runLater(() -> interpolationPoints.getItems().remove(affectedRow.getItem()));
        };

        updateReport(action, nodesToBlockOnUpdate);
    }

    private void handleRowAddedEvent(Event event) {
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
    public UUID getUpdatingElementId() {
        return ((ReportUpdater) getParent()).getUpdatingElementId();
    }

    @Override
    public Sample getSample() {
        return ((ReportContainer) getParent()).getSample();
    }
}

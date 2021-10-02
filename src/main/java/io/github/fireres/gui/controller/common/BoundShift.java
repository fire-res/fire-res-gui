package io.github.fireres.gui.controller.common;

import io.github.fireres.core.model.Point;
import io.github.fireres.core.model.Report;
import io.github.fireres.core.model.Sample;
import io.github.fireres.core.properties.ReportProperties;
import io.github.fireres.gui.controller.AbstractReportUpdaterComponent;
import io.github.fireres.gui.controller.ChartContainer;
import io.github.fireres.gui.controller.ReportContainer;
import io.github.fireres.gui.controller.ReportUpdater;
import io.github.fireres.gui.controller.SampleContainer;
import io.github.fireres.gui.controller.modal.BoundShiftModalWindow;
import io.github.fireres.gui.service.FxmlLoadService;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
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
import java.util.function.Consumer;
import java.util.function.Function;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@SuppressWarnings({"rawtypes"})
@Component
@Scope(scopeName = SCOPE_PROTOTYPE)
@FxmlView("boundShift.fxml")
@RequiredArgsConstructor
public class BoundShift extends AbstractReportUpdaterComponent<TitledPane>
        implements SampleContainer, ReportContainer {

    @FXML
    private TableColumn<Point<?>, Integer> timeColumn;

    @FXML
    private TableColumn<Point<?>, Integer> valueColumn;

    @FXML
    private Label label;


    @FXML
    @Getter
    private TableView<Point<?>> boundShiftTable;

    @Setter
    @Getter
    private Consumer<Point<?>> shiftAddedConsumer;

    @Setter
    private Consumer<Point<?>> shiftRemovedConsumer;

    @Getter
    @Setter
    private BiFunction<Integer, Number, Point<?>> shiftPointConstructor;

    @Setter
    @Getter
    private List<Node> nodesToBlockOnUpdate;

    @Setter
    private Function<ReportProperties, io.github.fireres.core.model.BoundShift<?>> propertyMapper;

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

        boundShiftTable.setContextMenu(contextMenu);
    }

    private ContextMenu createTableContextMenu() {
        val contextMenu = new ContextMenu();
        val addPointMenuItem = new MenuItem("Добавить");

        addPointMenuItem.setOnAction(this::handleRowAddedEvent);
        contextMenu.getItems().add(addPointMenuItem);

        return contextMenu;
    }

    private void initializeRowContextMenu() {
        boundShiftTable.setRowFactory(
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

    private void handleRowAddedEvent(Event event) {
        fxmlLoadService.loadComponent(BoundShiftModalWindow.class, this).getWindow().show();
    }

    private void handleRowDeletedEvent(TableRow<Point<?>> row) {
        Runnable action = () -> {
            shiftRemovedConsumer.accept(row.getItem());
            Platform.runLater(() -> boundShiftTable.getItems().remove(row.getItem()));
        };

        updateReport(action, nodesToBlockOnUpdate);
    }

    public void setLabel(String label) {
        this.label.setText(label);
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

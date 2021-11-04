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
import io.github.fireres.gui.framework.controller.modal.BoundShiftModalWindow;
import io.github.fireres.core.model.Point;
import io.github.fireres.core.model.Report;
import io.github.fireres.core.model.Sample;
import io.github.fireres.core.properties.ReportProperties;
import io.github.fireres.gui.framework.service.FxmlLoadService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
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
    @ColumnProperty("time")
    private TableColumn<Point<?>, Integer> timeColumn;

    @FXML
    @ColumnProperty("value")
    private TableColumn<Point<?>, Integer> valueColumn;

    @FXML
    private Label label;

    @FXML
    @Getter
    @TableContextMenu(
            table = @ContextMenu({
                    @ContextMenu.Item(text = "Добавить", handler = "handlePointAddPressed")
            }),
            row = @ContextMenu({
                    @ContextMenu.Item(text = "Добавить", handler = "handlePointAddPressedOnRow"),
                    @ContextMenu.Item(text = "Удалить", handler = "handleRowDeletePressed")
            })
    )
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

    @ContextMenu.Handler
    public void handlePointAddPressedOnRow(TableRow<Point<?>> row) {
        handlePointAddPressed();
    }

    @ContextMenu.Handler
    public void handlePointAddPressed() {
        fxmlLoadService.loadComponent(BoundShiftModalWindow.class, this).getWindow().show();
    }

    @ContextMenu.Handler
    public void handleRowDeletePressed(TableRow<Point<?>> row) {
        updateReport(() -> {
            shiftRemovedConsumer.accept(row.getItem());
            Platform.runLater(() -> boundShiftTable.getItems().remove(row.getItem()));
        }, nodesToBlockOnUpdate);
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
    public UUID getReportId() {
        return ((ReportUpdater) getParent()).getReportId();
    }

    @Override
    public Sample getSample() {
        return ((ReportContainer) getParent()).getSample();
    }

}

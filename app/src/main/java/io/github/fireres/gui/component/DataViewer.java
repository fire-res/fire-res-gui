package io.github.fireres.gui.component;

import io.github.fireres.excel.core.model.Column;
import io.github.fireres.excel.core.model.ExcelReport;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.layout.StackPane;
import javafx.scene.text.TextAlignment;
import lombok.val;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DataViewer extends TableView<Map<String, Number>> {

    public DataViewer(ExcelReport excelReport) {
        val data = excelReport.getData();

        initializeColumns(data);
        initializeData(data);

        this.setColumnResizePolicy(CONSTRAINED_RESIZE_POLICY);
    }

    private void initializeColumns(List<Column> data) {
        if (data.isEmpty()) {
            return;
        }

        for (int i = 0; i < data.size(); i++) {
            val excelColumn = data.get(i);
            val column = new TableColumn<Map<String, Number>, Number>(excelColumn.getHeader());
            makeHeaderWrappable(column);

            column.setCellValueFactory(new MapValueFactory(excelColumn.getHeader()));

            this.getColumns().add(column);
        }
    }

    private void initializeData(List<Column> data) {
        if (data.isEmpty()) {
            return;
        }

        for (int i = 0; i < data.get(0).getValues().size(); i++) {
            val index = i;

            Map<String, Number> mappedData = data.stream()
                    .collect(Collectors.toMap(
                            Column::getHeader,
                            column -> column.getValues().get(index)));

            this.getItems().add(mappedData);
        }
    }

    private void makeHeaderWrappable(TableColumn column) {
        Label label = new Label(column.getText());
        label.setWrapText(true);
        label.setAlignment(Pos.CENTER);
        label.setTextAlignment(TextAlignment.CENTER);

        StackPane stack = new StackPane();
        stack.getChildren().add(label);
        stack.prefWidthProperty().bind(column.widthProperty().subtract(5));
        label.prefWidthProperty().bind(stack.prefWidthProperty());

        column.setGraphic(stack);
    }

}

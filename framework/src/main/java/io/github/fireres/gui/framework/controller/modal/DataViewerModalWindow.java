package io.github.fireres.gui.framework.controller.modal;

import io.github.fireres.gui.framework.annotation.FxmlView;
import io.github.fireres.gui.framework.annotation.ModalWindow;
import io.github.fireres.gui.framework.controller.AbstractComponent;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Getter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Map;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@FxmlView("dataViewerModalWindow.fxml")
@ModalWindow(
        title = "Данные отчета",
        resizable = true,
        isAlwaysOnTop = true,
        modality = Modality.NONE)
@Component
@Scope(scopeName = SCOPE_PROTOTYPE)
public class DataViewerModalWindow extends AbstractComponent<AnchorPane> {

    @ModalWindow.Window
    @Getter
    private Stage window;

    public void setDataViewer(TableView<Map<String, Number>> dataViewer) {
        this.getComponent().getChildren().removeIf(node -> node instanceof TableView);
        this.getComponent().getChildren().add(dataViewer);

        AnchorPane.setBottomAnchor(dataViewer, 0d);
        AnchorPane.setTopAnchor(dataViewer, 0d);
        AnchorPane.setLeftAnchor(dataViewer, 0d);
        AnchorPane.setRightAnchor(dataViewer, 0d);
    }

}

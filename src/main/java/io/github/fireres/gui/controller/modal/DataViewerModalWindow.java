package io.github.fireres.gui.controller.modal;

import io.github.fireres.gui.annotation.FxmlView;
import io.github.fireres.gui.annotation.ModalWindow;
import io.github.fireres.gui.component.DataViewer;
import io.github.fireres.gui.controller.AbstractComponent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Getter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

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

    public void setDataViewer(DataViewer dataViewer) {
        this.getComponent().getChildren().removeIf(node -> node instanceof DataViewer);
        this.getComponent().getChildren().add(dataViewer);

        AnchorPane.setBottomAnchor(dataViewer, 0d);
        AnchorPane.setTopAnchor(dataViewer, 0d);
        AnchorPane.setLeftAnchor(dataViewer, 0d);
        AnchorPane.setRightAnchor(dataViewer, 0d);
    }

}

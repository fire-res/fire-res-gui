package io.github.fireres.gui.framework.controller.modal;

import io.github.fireres.gui.framework.annotation.FxmlView;
import io.github.fireres.gui.framework.annotation.ModalWindow;
import io.github.fireres.gui.framework.config.properties.general.GeneralProperties;
import io.github.fireres.gui.framework.controller.AbstractComponent;
import io.github.fireres.gui.framework.model.Logos;
import io.github.fireres.gui.framework.service.HostServicesProvider;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@ModalWindow(title = "О программе")
@FxmlView("aboutProgramModalWindow.fxml")
@RequiredArgsConstructor
@Component
@Scope(scopeName = SCOPE_PROTOTYPE)
public class AboutProgramModalWindow extends AbstractComponent<AnchorPane> {

    @FXML
    private Label version;

    @FXML
    private ImageView imageView;

    @ModalWindow.Window
    @Getter
    private Stage window;

    @FXML
    private Hyperlink repositoryLink;

    private final HostServicesProvider hostServicesProvider;
    private final Logos logos;
    private final GeneralProperties generalProperties;

    @Override
    public void postConstruct() {
        imageView.setImage(logos.getLogo50());
        repositoryLink.setText(generalProperties.getRepositoryLink());
        version.setText(generalProperties.getVersion());
    }

    public void openGithubLink() {
        hostServicesProvider.get().showDocument(repositoryLink.getText());
    }

}


package io.github.fireres.gui.framework.initializer.general;

import io.github.fireres.gui.framework.config.properties.general.PrimaryStageProperties;
import io.github.fireres.gui.framework.initializer.Initializer;
import io.github.fireres.gui.framework.model.Logos;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PrimaryStageInitializer implements Initializer<Stage> {

    private final Logos logos;
    private final PrimaryStageProperties properties;

    @SneakyThrows
    @Override
    public void initialize(Stage stage) {
        stage.setTitle(properties.getTitle());
        stage.setMinWidth(properties.getMinWidth());
        stage.setMinHeight(properties.getMinHeight());
        stage.setResizable(properties.getResizable());
        stage.setMaximized(properties.getMaximized());
        stage.requestFocus();
        stage.getIcons().add(logos.getLogo512());
    }

}

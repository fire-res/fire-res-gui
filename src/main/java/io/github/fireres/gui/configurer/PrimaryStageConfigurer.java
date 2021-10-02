package io.github.fireres.gui.configurer;

import io.github.fireres.gui.config.properties.ApplicationProperties;
import io.github.fireres.gui.model.Logos;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PrimaryStageConfigurer implements Configurer<Stage> {

    private final Logos logos;
    private final ApplicationProperties applicationProperties;

    @SneakyThrows
    @Override
    public void config(Stage stage) {
        val properties = applicationProperties.getPrimaryStage();

        stage.setTitle(properties.getTitle());
        stage.setMinWidth(properties.getMinWidth());
        stage.setMinHeight(properties.getMinHeight());
        stage.setResizable(properties.getResizable());
        stage.setMaximized(properties.getMaximized());
        stage.requestFocus();
        stage.getIcons().add(logos.getLogo512());
    }

}

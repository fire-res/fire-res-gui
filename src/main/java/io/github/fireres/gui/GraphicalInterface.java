package io.github.fireres.gui;

import io.github.fireres.gui.controller.common.MainScene;
import io.github.fireres.gui.initializer.general.PrimaryStageInitializer;
import io.github.fireres.gui.service.FxmlLoadService;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GraphicalInterface {

    private final PrimaryStageInitializer primaryStageInitializer;
    private final FxmlLoadService fxmlLoadService;

    public void start(Stage stage) {
        primaryStageInitializer.initialize(stage);

        val mainScene = fxmlLoadService.loadComponent(MainScene.class);

        stage.setScene(new Scene(mainScene.getComponent()));
        stage.show();
    }

}

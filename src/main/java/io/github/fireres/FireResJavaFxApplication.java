package io.github.fireres;

import io.github.fireres.controller.TestController;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

@Slf4j
public class FireResJavaFxApplication extends Application {

    private ConfigurableApplicationContext applicationContext;

    @Override
    public void init() {
        val args = getParameters().getRaw().toArray(new String[0]);

        this.applicationContext = SpringApplication.run(FireResSpringApplication.class, args);
    }

    @Override
    public void start(Stage stage) {
        val fxWeaver = applicationContext.getBean(FxWeaver.class);
        val root = (Parent) fxWeaver.loadView(TestController.class);
        val scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }

}

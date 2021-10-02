package io.github.fireres.gui;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.ExecutorService;

@Slf4j
public class FireResJavaFxApplication extends Application {

    public static HostServices hostServices;

    private ConfigurableApplicationContext applicationContext;
    private ExecutorService executorService;
    private GraphicalInterface gui;

    @Override
    public void start(Stage stage) {
        val args = getParameters().getRaw().toArray(new String[0]);

        this.applicationContext = SpringApplication.run(FireResSpringApplication.class, args);
        this.executorService = applicationContext.getBean(ExecutorService.class);
        this.gui = applicationContext.getBean(GraphicalInterface.class);
        hostServices = this.getHostServices();

        gui.start(stage);
    }

    @Override
    public void stop() throws Exception {
        executorService.shutdownNow();
        applicationContext.stop();
    }

}

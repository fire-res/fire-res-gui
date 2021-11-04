package io.github.fireres.gui;

import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "io.github.fireres")
@ConfigurationPropertiesScan("io.github.fireres")
public class FireResSpringApplication {

    public static void main(String[] args) {
        Application.launch(FireResJavaFxApplication.class, args);
    }

}

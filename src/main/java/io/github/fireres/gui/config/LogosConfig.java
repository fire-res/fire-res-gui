package io.github.fireres.gui.config;

import io.github.fireres.gui.model.Logos;
import javafx.scene.image.Image;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LogosConfig {

    @SuppressWarnings("ConstantConditions")
    @SneakyThrows
    @Bean
    public Logos loadLogos() {
        return new Logos(
                new Image(getClass().getResource("/io/github/fireres/gui/controller/image/logo-512.png").openStream()),
                new Image(getClass().getResource("/io/github/fireres/gui/controller/image/logo-50.png").openStream())
        );
    }

}

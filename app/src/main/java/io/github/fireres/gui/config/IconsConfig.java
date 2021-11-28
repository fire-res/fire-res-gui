package io.github.fireres.gui.config;

import io.github.fireres.gui.framework.model.Icons;
import javafx.scene.image.Image;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IconsConfig {

    @SuppressWarnings("ConstantConditions")
    @SneakyThrows
    @Bean
    public Icons loadLogos() {
        return new Icons(
                new Image(getClass().getResource("/image/logo-512.png").openStream()),
                new Image(getClass().getResource("/image/logo-50.png").openStream()),
                new Image(getClass().getResource("/image/warning-48.png").openStream())
        );
    }

}

package io.github.fireres.gui.framework.config;

import com.rits.cloning.Cloner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClonerConfig {

    @Bean
    public Cloner getCloner() {
        return new Cloner();
    }

}

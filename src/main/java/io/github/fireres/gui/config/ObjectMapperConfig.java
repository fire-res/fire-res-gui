package io.github.fireres.gui.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.github.fireres.excess.pressure.properties.ExcessPressureProperties;
import io.github.fireres.firemode.properties.FireModeProperties;
import io.github.fireres.heatflow.properties.HeatFlowProperties;
import io.github.fireres.unheated.surface.properties.UnheatedSurfaceProperties;
import lombok.val;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObjectMapperConfig {

    public static final Class[] REPORT_PROPERTIES_CLASSES = new Class[]{
            FireModeProperties.class,
            ExcessPressureProperties.class,
            HeatFlowProperties.class,
            UnheatedSurfaceProperties.class
    };

    @Bean
    public ObjectMapper getObjectMapper() {
        val mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        val module = new SimpleModule();

        module.registerSubtypes(REPORT_PROPERTIES_CLASSES);
        mapper.registerModule(module);

        return mapper;
    }

}

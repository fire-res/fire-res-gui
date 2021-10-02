package io.github.fireres.gui.preset;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.github.fireres.core.properties.ReportProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Preset {

    @Builder.Default
    private Boolean applyingByDefault = false;

    private String description;

    @JsonDeserialize(keyUsing = PresetPropertiesKeyDeserializer.class)
    @Builder.Default
    private Map<Class<? extends ReportProperties>, ReportProperties> properties = new HashMap<>();

    public <T extends ReportProperties> T getProperties(Class<T> propertiesClass) {
        if (properties.containsKey(propertiesClass)) {
            return (T) properties.get(propertiesClass);
        } else {
            return getDefaultProperties(propertiesClass);
        }
    }

    @SneakyThrows
    private <T extends ReportProperties> T getDefaultProperties(Class<T> propertiesClass) {
        return propertiesClass.getDeclaredConstructor(new Class[]{}).newInstance();
    }

    @Override
    public String toString() {
        return getDescription();
    }
}

package io.github.fireres.gui.framework.preset;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.fireres.core.properties.ReportProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Preset {

    @Builder.Default
    private Boolean applyingByDefault = false;

    private String description;

    @Builder.Default
    private List<ReportProperties> properties = new ArrayList<>();

    @Override
    public String toString() {
        return getDescription();
    }

    @JsonIgnore
    public <P extends ReportProperties> List<P> findAll(Class<P> propertiesClass) {
        return properties.stream()
                .filter(p -> p.getClass().equals(propertiesClass))
                .map(p -> (P) p)
                .collect(Collectors.toList());
    }

    @JsonIgnore
    public <P extends ReportProperties> Optional<P> findFirst(Class<P> propertiesClass) {
        return properties.stream()
                .filter(p -> p.getClass().equals(propertiesClass))
                .map(p -> (P) p)
                .findFirst();
    }

}

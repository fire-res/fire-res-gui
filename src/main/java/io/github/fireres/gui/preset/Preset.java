package io.github.fireres.gui.preset;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.fireres.core.properties.ReportProperties;
import io.github.fireres.excess.pressure.properties.ExcessPressureProperties;
import io.github.fireres.firemode.properties.FireModeProperties;
import io.github.fireres.heatflow.properties.HeatFlowProperties;
import io.github.fireres.unheated.surface.properties.UnheatedSurfaceProperties;
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
    public Optional<FireModeProperties> getFireModeProperties() {
        return findFirst(FireModeProperties.class);
    }

    @JsonIgnore
    public Optional<ExcessPressureProperties> getExcessPressureProperties() {
        return findFirst(ExcessPressureProperties.class);
    }

    @JsonIgnore
    public Optional<HeatFlowProperties> getHeatFlowProperties() {
        return findFirst(HeatFlowProperties.class);
    }

    @JsonIgnore
    public List<UnheatedSurfaceProperties> getUnheatedSurfaceProperties() {
        return findAll(UnheatedSurfaceProperties.class);
    }

    private <P extends ReportProperties> List<P> findAll(Class<P> propertiesClass) {
        return properties.stream()
                .filter(p -> p.getClass().equals(propertiesClass))
                .map(p -> (P) p)
                .collect(Collectors.toList());
    }

    private <P extends ReportProperties> Optional<P> findFirst(Class<P> propertiesClass) {
        return properties.stream()
                .filter(p -> p.getClass().equals(propertiesClass))
                .map(p -> (P) p)
                .findFirst();
    }

}

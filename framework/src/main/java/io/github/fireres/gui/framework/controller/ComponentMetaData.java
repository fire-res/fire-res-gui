package io.github.fireres.gui.framework.controller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComponentMetaData {

    private boolean annotationProcessed;
    private boolean postConstructed;
    private boolean hierarchyInitialized;

}

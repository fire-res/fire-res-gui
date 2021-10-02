package io.github.fireres.gui.model;

import io.github.fireres.gui.controller.ChartContainer;
import javafx.scene.Node;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportTask {

    private UUID updatingElementId;

    private List<ChartContainer> chartContainers;

    @Builder.Default
    private List<Node> nodesToLock = new ArrayList<>();

    private Runnable action;

}

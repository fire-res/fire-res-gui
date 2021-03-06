package io.github.fireres.gui.framework.model;

import io.github.fireres.gui.framework.controller.ChartContainer;
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

    private UUID reportId;

    private List<ChartContainer> chartContainers;

    @Builder.Default
    private List<Node> nodesToLock = new ArrayList<>();

    private Runnable action;

}

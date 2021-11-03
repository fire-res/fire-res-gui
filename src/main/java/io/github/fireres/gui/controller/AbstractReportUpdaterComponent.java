package io.github.fireres.gui.controller;

import io.github.fireres.gui.model.ReportTask;
import io.github.fireres.gui.service.ReportExecutorService;
import javafx.scene.Node;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

import static java.util.Collections.singletonList;

public abstract class AbstractReportUpdaterComponent<N>
        extends AbstractComponent<N>
        implements ReportUpdater {

    @Autowired
    private ReportExecutorService reportExecutorService;

    @Override
    public void updateReport(Runnable action, Node... nodesToLock) {
        updateReport(action, Arrays.asList(nodesToLock));
    }

    @Override
    public void updateReport(Runnable action, List<Node> nodesToLock) {
        reportExecutorService.runTask(ReportTask.builder()
                .reportId(getReportId())
                .action(action)
                .chartContainers(singletonList(getChartContainer()))
                .nodesToLock(nodesToLock)
                .build());
    }

}

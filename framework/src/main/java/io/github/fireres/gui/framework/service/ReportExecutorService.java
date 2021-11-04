package io.github.fireres.gui.framework.service;

import io.github.fireres.gui.framework.model.ReportTask;

public interface ReportExecutorService {

    void runTask(ReportTask task);

    void addListener(ReportUpdateListener listener);

    void removeListener(ReportUpdateListener listener);

}

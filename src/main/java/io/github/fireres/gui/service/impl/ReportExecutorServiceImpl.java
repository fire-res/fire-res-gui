package io.github.fireres.gui.service.impl;

import io.github.fireres.gui.controller.ChartContainer;
import io.github.fireres.gui.controller.common.GeneralParams;
import io.github.fireres.gui.exception.NotNotifiableException;
import io.github.fireres.gui.model.ReportTask;
import io.github.fireres.gui.service.ReportExecutorService;
import io.github.fireres.gui.service.ReportUpdateListener;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.text.TextAlignment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportExecutorServiceImpl implements ReportExecutorService {


    private final Map<UUID, Lock> locks = new ConcurrentHashMap<>();
    private final Map<UUID, AtomicInteger> tasksRunning = new ConcurrentHashMap<>();
    private final List<ReportUpdateListener> reportUpdateListeners = new ArrayList<>();

    private final ExecutorService executorService;
    private final GeneralParams generalParams;

    @Override
    public void runTask(ReportTask task) {
        val taskUpdatingElementId = task.getUpdatingElementId();
        val nodesToLock = task.getNodesToLock();

        locks.putIfAbsent(taskUpdatingElementId, new ReentrantLock(true));
        tasksRunning.putIfAbsent(taskUpdatingElementId, new AtomicInteger(0));

        if (tasksRunning.get(taskUpdatingElementId).get() == 0) {
            showProgressIndicator(task.getChartContainers());
            lockElements(nodesToLock);
            lockGeneralParams();
        }

        tasksRunning.get(taskUpdatingElementId).incrementAndGet();

        executorService.submit(() -> {
            val lock = locks.get(taskUpdatingElementId);
            removeErrorNotification(task.getChartContainers());

            try {
                if (lock.tryLock(15, TimeUnit.SECONDS)) {
                    doTask(task);
                }
            } catch (NotNotifiableException e) {
                log.error("Error while executing task with id: {}", taskUpdatingElementId, e);
            } catch (Exception e) {
                log.error("Error while executing task with id: {}", taskUpdatingElementId, e);
                showErrorNotification(task.getChartContainers());
            } finally {
                if (tasksRunning.get(taskUpdatingElementId).decrementAndGet() == 0) {
                    unlockElements(nodesToLock);
                    removeProgressIndicator(task.getChartContainers());
                }

                if (tasksRunning.values().stream().allMatch(count -> count.get() == 0)) {
                    unlockGeneralParams();
                }

                lock.unlock();
            }
        });
    }

    @Override
    public void addListener(ReportUpdateListener listener) {
        reportUpdateListeners.add(listener);
    }

    @Override
    public void removeListener(ReportUpdateListener listener) {
        reportUpdateListeners.remove(listener);
    }

    private void unlockElements(List<javafx.scene.Node> nodesToLock) {
        Platform.runLater(() -> nodesToLock.forEach(node -> node.setDisable(false)));
    }

    private void doTask(ReportTask task) {
        reportUpdateListeners.forEach(listener -> listener.preUpdate(task.getUpdatingElementId()));

        task.getAction().run();
        Platform.runLater(() -> task.getChartContainers().forEach(ChartContainer::synchronizeChart));

        reportUpdateListeners.forEach(listener -> listener.postUpdate(task.getUpdatingElementId()));
    }

    private void lockElements(List<javafx.scene.Node> nodesToLock) {
        nodesToLock.forEach(node -> node.setDisable(true));
    }

    private void lockGeneralParams() {
        generalParams.getComponent().setDisable(true);
    }

    private void unlockGeneralParams() {
        Platform.runLater(() -> generalParams.getComponent().setDisable(false));
    }

    private void showProgressIndicator(List<ChartContainer> chartContainers) {
        Platform.runLater(() -> chartContainers.forEach(chartContainer -> {
            chartContainer.getChart().setDisable(true);
            chartContainer.getChart().setEffect(new GaussianBlur());
            chartContainer.getStackPane().getChildren().add(new ProgressIndicator());
        }));
    }

    private void removeProgressIndicator(List<ChartContainer> chartContainers) {
        Platform.runLater(() -> chartContainers.forEach(chartContainer -> {
            chartContainer.getStackPane().getChildren().removeIf(child -> child instanceof ProgressIndicator);

            if (chartContainer.getStackPane().getChildren().size() == 1) {
                chartContainer.getChart().setDisable(false);
                chartContainer.getChart().setEffect(null);
            }
        }));
    }

    private void showErrorNotification(List<ChartContainer> chartContainers) {
        val error = new Label("Невозможно сгенерировать отчет с данными параметрами.\n"
                + "Измените параметры или обновите график.");

        error.setStyle("-fx-font-size: 15; -fx-font-weight: bold;");
        error.setTextAlignment(TextAlignment.CENTER);

        Platform.runLater(() -> chartContainers.forEach(chartContainer -> {
            chartContainer.getChart().setDisable(true);
            chartContainer.getChart().setEffect(new GaussianBlur());
            chartContainer.getStackPane().getChildren().add(error);
        }));
    }

    private void removeErrorNotification(List<ChartContainer> chartContainers) {
        Platform.runLater(() -> chartContainers.forEach(chartContainer -> {
            chartContainer.getStackPane().getChildren().removeIf(child -> child instanceof Label);

            if (chartContainer.getStackPane().getChildren().size() == 1) {
                chartContainer.getChart().setDisable(false);
                chartContainer.getChart().setEffect(null);
            }
        }));
    }
}

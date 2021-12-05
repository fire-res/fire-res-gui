package io.github.fireres.gui.framework.controller;

import io.github.fireres.gui.framework.controller.common.MainScene;
import io.github.fireres.gui.framework.controller.modal.HandledExceptionModalWindow;
import io.github.fireres.gui.framework.service.FxmlLoadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExceptionHandler implements Thread.UncaughtExceptionHandler {

    private final FxmlLoadService fxmlLoadService;
    private final MainScene mainScene;

    private final AtomicBoolean windowIsShown = new AtomicBoolean(false);

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        log.error("Uncaught exception has been acquired: ", e);

        if (!windowIsShown.get()) {
            val modalWindow = fxmlLoadService.loadComponent(
                    HandledExceptionModalWindow.class,
                    mainScene,
                    w -> w.setException(getRootCause(e))
            );

            windowIsShown.set(true);
            modalWindow.getWindow().setOnCloseRequest(event -> windowIsShown.set(false));
            modalWindow.getWindow().show();
        }
    }

    private Throwable getRootCause(Throwable e) {
        var cause = e;

        while (cause.getCause() != null) {
            cause = cause.getCause();
        }

        return cause;
    }

}

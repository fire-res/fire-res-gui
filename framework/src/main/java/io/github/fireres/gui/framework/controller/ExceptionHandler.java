package io.github.fireres.gui.framework.controller;

import io.github.fireres.gui.framework.controller.common.MainScene;
import io.github.fireres.gui.framework.controller.modal.HandledExceptionModalWindow;
import io.github.fireres.gui.framework.service.FxmlLoadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExceptionHandler implements Thread.UncaughtExceptionHandler {

    private final FxmlLoadService fxmlLoadService;
    private final MainScene mainScene;

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        log.info("Exception handled: " + e.getMessage() + " | " + Arrays.toString(e.getStackTrace()));

        fxmlLoadService.loadComponent(
                HandledExceptionModalWindow.class,
                mainScene,
                handledExceptionModalWindow -> handledExceptionModalWindow.setException(e)
        ).getWindow().show();
    }

}

package io.github.fireres.gui.framework.controller;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Handler implements Thread.UncaughtExceptionHandler {

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        log.info("Exception handled: " + e.toString());
    }
}

package io.github.fireres.gui.framework.service;

import java.util.UUID;

public interface ReportUpdateListener {

    default void preUpdate(UUID elementId) {
    }

    default void postUpdate(UUID elementId) {
    }

}

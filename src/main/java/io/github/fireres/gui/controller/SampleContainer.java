package io.github.fireres.gui.controller;

import io.github.fireres.core.model.Sample;

public interface SampleContainer {

    Sample getSample();

    default void generateReports() {
    }

}

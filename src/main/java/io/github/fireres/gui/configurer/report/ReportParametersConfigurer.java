package io.github.fireres.gui.configurer.report;

import io.github.fireres.gui.controller.ReportContainer;
import io.github.fireres.gui.preset.Preset;

public interface ReportParametersConfigurer<T extends ReportContainer<?, ?>> {

    void config(T reportContainer, Preset preset);

}

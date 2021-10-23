package io.github.fireres.gui.preset;

import io.github.fireres.core.properties.FunctionForm;
import io.github.fireres.gui.controller.common.FunctionParams;

public interface FunctionFormApplier {

    void apply(FunctionParams functionParams, FunctionForm<?> functionForm);

}

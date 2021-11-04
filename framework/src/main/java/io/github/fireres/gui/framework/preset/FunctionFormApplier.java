package io.github.fireres.gui.framework.preset;

import io.github.fireres.core.properties.FunctionForm;
import io.github.fireres.gui.framework.controller.common.FunctionParams;

public interface FunctionFormApplier {

    void apply(FunctionParams functionParams, FunctionForm<?> functionForm);

}

package io.github.fireres.gui.framework.preset;

import io.github.fireres.core.properties.BoundsShift;
import io.github.fireres.gui.framework.controller.common.BoundsShiftParams;

public interface BoundsShiftApplier {

    void apply(BoundsShiftParams boundsShiftParams, BoundsShift boundsShift);

}

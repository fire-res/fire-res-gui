package io.github.fireres.gui.framework.preset.impl;

import io.github.fireres.core.properties.BoundsShift;
import io.github.fireres.gui.framework.controller.common.BoundShift;
import io.github.fireres.gui.framework.controller.common.BoundsShiftParams;
import io.github.fireres.gui.framework.preset.BoundsShiftApplier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class BoundsShiftApplierImpl implements BoundsShiftApplier {

    @Override
    public void apply(BoundsShiftParams boundsShiftParams, BoundsShift boundsShift) {
        clearTables(boundsShiftParams);
    }

    private void clearTables(BoundsShiftParams boundsShiftParams) {
        boundsShiftParams
                .getChildren(BoundShift.class)
                .forEach(boundShiftComponent -> boundShiftComponent.getBoundShiftTable().getItems().clear());
    }

}

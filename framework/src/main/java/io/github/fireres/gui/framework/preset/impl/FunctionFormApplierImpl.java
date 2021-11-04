package io.github.fireres.gui.framework.preset.impl;

import io.github.fireres.core.model.Point;
import io.github.fireres.core.properties.FunctionForm;
import io.github.fireres.gui.framework.config.properties.general.FunctionFormCoefficientProperties;
import io.github.fireres.gui.framework.controller.common.FunctionParams;
import io.github.fireres.gui.framework.preset.FunctionFormApplier;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FunctionFormApplierImpl implements FunctionFormApplier {

    private final FunctionFormCoefficientProperties properties;

    @Override
    public void apply(FunctionParams functionParams, FunctionForm<?> functionForm) {
        setLinearityCoefficient(functionParams.getLinearityCoefficient(), functionForm);
        setDispersionCoefficient(functionParams.getDispersionCoefficient(), functionForm);
        setChildFunctionsDeltaCoefficient(functionParams.getChildFunctionsDeltaCoefficient(), functionForm);
        setInterpolationPoints(functionParams.getInterpolationPoints(), functionForm);
    }

    private void setLinearityCoefficient(Spinner<Double> linearCoefficient, FunctionForm<?> functionForm) {
        linearCoefficient.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(
                properties.getMinValue(),
                properties.getMaxValue(),
                functionForm.getLinearityCoefficient(),
                properties.getIncrement()));
    }

    private void setDispersionCoefficient(Spinner<Double> dispersionCoefficient, FunctionForm<?> functionForm) {
        dispersionCoefficient.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(
                properties.getMinValue(),
                properties.getMaxValue(),
                functionForm.getDispersionCoefficient(),
                properties.getIncrement()));
    }

    private void setChildFunctionsDeltaCoefficient(Spinner<Double> childFunctionsDeltaCoefficient,
                                                   FunctionForm<?> functionForm) {

        childFunctionsDeltaCoefficient.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(
                properties.getMinValue(),
                properties.getMaxValue(),
                functionForm.getChildFunctionsDeltaCoefficient(),
                properties.getIncrement()));
    }

    private void setInterpolationPoints(TableView<Point<?>> interpolationPoints, FunctionForm<?> functionForm) {
        interpolationPoints.getItems().clear();

        for (Point<?> interpolationPoint : functionForm.getInterpolationPoints()) {
            interpolationPoints.getItems().add(interpolationPoint);
        }
    }

}

package io.github.fireres.gui.configurer.report;

import io.github.fireres.core.model.Point;
import io.github.fireres.core.properties.FunctionForm;
import io.github.fireres.gui.controller.ReportContainer;
import io.github.fireres.gui.controller.common.FunctionParams;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableView;

public abstract class AbstractReportParametersConfigurer<T extends ReportContainer<?, ?>>
        implements ReportParametersConfigurer<T> {

    private static final Double COEFFICIENT_MIN = 0d;
    private static final Double COEFFICIENT_MAX = 1d;
    private static final Double COEFFICIENT_INCREMENT = 0.01;

    protected void resetFunctionParameters(FunctionParams functionParams, FunctionForm<?> functionForm) {
        resetLinearCoefficient(functionParams.getLinearityCoefficient(), functionForm);
        resetDispersionCoefficient(functionParams.getDispersionCoefficient(), functionForm);
        resetChildFunctionsDeltaCoefficient(functionParams.getChildFunctionsDeltaCoefficient(), functionForm);
        resetInterpolationPoints(functionParams.getInterpolationPoints(), functionForm);
    }

    private void resetLinearCoefficient(Spinner<Double> linearCoefficient, FunctionForm<?> functionForm) {
        linearCoefficient.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(
                COEFFICIENT_MIN,
                COEFFICIENT_MAX,
                functionForm.getLinearityCoefficient(),
                COEFFICIENT_INCREMENT));
    }

    private void resetDispersionCoefficient(Spinner<Double> dispersionCoefficient, FunctionForm<?> functionForm) {
        dispersionCoefficient.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(
                COEFFICIENT_MIN,
                COEFFICIENT_MAX,
                functionForm.getDispersionCoefficient(),
                COEFFICIENT_INCREMENT));
    }

    private void resetChildFunctionsDeltaCoefficient(Spinner<Double> childFunctionsDeltaCoefficient,
                                                     FunctionForm<?> functionForm) {

        childFunctionsDeltaCoefficient.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(
                COEFFICIENT_MIN,
                COEFFICIENT_MAX,
                functionForm.getChildFunctionsDeltaCoefficient(),
                COEFFICIENT_INCREMENT));
    }

    private void resetInterpolationPoints(TableView<Point<?>> interpolationPoints, FunctionForm<?> functionForm) {
        interpolationPoints.getItems().clear();

        for (Point<?> interpolationPoint : functionForm.getInterpolationPoints()) {
            interpolationPoints.getItems().add(interpolationPoint);
        }
    }

}

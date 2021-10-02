package io.github.fireres.gui.controller.common;

import io.github.fireres.core.model.Point;
import io.github.fireres.core.model.Report;
import io.github.fireres.core.model.Sample;
import io.github.fireres.core.properties.ReportProperties;
import io.github.fireres.gui.annotation.LoadableComponent;
import io.github.fireres.gui.controller.AbstractReportUpdaterComponent;
import io.github.fireres.gui.controller.ChartContainer;
import io.github.fireres.gui.controller.ReportContainer;
import io.github.fireres.gui.controller.ReportUpdater;
import io.github.fireres.gui.controller.SampleContainer;
import io.github.fireres.gui.service.FxmlLoadService;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import lombok.RequiredArgsConstructor;
import lombok.val;
import io.github.fireres.gui.annotation.FxmlView;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@SuppressWarnings("rawtypes")
@Component
@Scope(scopeName = SCOPE_PROTOTYPE)
@FxmlView("boundsShiftParams.fxml")
@RequiredArgsConstructor
public class BoundsShiftParams extends AbstractReportUpdaterComponent<TitledPane>
        implements SampleContainer, ReportContainer {

    @FXML
    private VBox boundsShiftVbox;

    private final FxmlLoadService fxmlLoadService;

    public void addBoundShift(
            String label,
            List<Node> nodesToBlockOnUpdate,
            Function<ReportProperties, io.github.fireres.core.model.BoundShift<?>> propertyMapper,
            Consumer<Point<?>> shiftAddedConsumer,
            Consumer<Point<?>> shiftRemovedConsumer,
            BiFunction<Integer, Number, Point<?>> shiftPointConstructor
    ) {
        val boundShift = fxmlLoadService.loadComponent(BoundShift.class, this);

        boundShift.setLabel(label);
        boundShift.setNodesToBlockOnUpdate(nodesToBlockOnUpdate);
        boundShift.setShiftAddedConsumer(shiftAddedConsumer);
        boundShift.setShiftRemovedConsumer(shiftRemovedConsumer);
        boundShift.setShiftPointConstructor(shiftPointConstructor);
        boundShift.setPropertyMapper(propertyMapper);

        boundsShiftVbox.getChildren().add(boundShift.getComponent());
    }

    @Override
    public Report getReport() {
        return ((ReportContainer) getParent()).getReport();
    }

    @Override
    public ChartContainer getChartContainer() {
        return ((ReportContainer) getParent()).getChartContainer();
    }

    @Override
    public UUID getUpdatingElementId() {
        return ((ReportUpdater) getParent()).getUpdatingElementId();
    }

    @Override
    public Sample getSample() {
        return ((ReportContainer) getParent()).getSample();
    }

}

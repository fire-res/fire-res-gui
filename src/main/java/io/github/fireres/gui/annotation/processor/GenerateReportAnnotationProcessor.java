package io.github.fireres.gui.annotation.processor;

import io.github.fireres.core.service.ReportCreatorService;
import io.github.fireres.gui.annotation.GenerateReport;
import io.github.fireres.gui.controller.ExtendedComponent;
import io.github.fireres.gui.controller.SampleContainer;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class GenerateReportAnnotationProcessor implements AnnotationProcessor {

    private final Map<Class<?>, ReportCreatorService<?>> reportCreators;

    @Override
    public void process(ExtendedComponent<?> component) {
        val fields = component.getClass().getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(GenerateReport.class)) {
                validateComponent(component);
                generateReportAndInject(field, ((SampleContainer) component));
            }
        }
    }

    private void validateComponent(ExtendedComponent<?> component) {
        if (!(component instanceof SampleContainer)) {
            throw new IllegalStateException("Component must implement SampleContainer interface");
        }
    }

    @SneakyThrows
    private void generateReportAndInject(Field field, SampleContainer component) {
        val reportCreator = lookupReportCreator(field.getType());
        val sample = component.getSample();
        val report = reportCreator.createReport(sample);

        field.setAccessible(true);
        field.set(component, report);
    }

    private ReportCreatorService<?> lookupReportCreator(Class<?> reportClass) {
        if (!reportCreators.containsKey(reportClass)) {
            throw new IllegalStateException("Report creator not found for class: " + reportClass.getSimpleName());
        }

        return reportCreators.get(reportClass);
    }

}

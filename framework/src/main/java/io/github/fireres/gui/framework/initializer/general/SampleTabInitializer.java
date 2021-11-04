package io.github.fireres.gui.framework.initializer.general;

import io.github.fireres.gui.framework.controller.ReportTab;
import io.github.fireres.gui.framework.controller.common.SampleTab;
import io.github.fireres.gui.framework.initializer.Initializer;
import io.github.fireres.gui.framework.service.FxmlLoadService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SampleTabInitializer implements Initializer<SampleTab> {

    private final List<Class<? extends ReportTab>> reportTabsClasses;
    private final FxmlLoadService fxmlLoadService;

    @Override
    public void initialize(SampleTab sampleTab) {
        reportTabsClasses.forEach(reportTabClass -> {
            val reportTab = fxmlLoadService.loadComponent(reportTabClass, sampleTab);

            if (!reportTab.isReportExcluded()) {
                sampleTab.getReportsTabPane().getTabs().add(reportTab.getComponent());
            }
        });
    }

}

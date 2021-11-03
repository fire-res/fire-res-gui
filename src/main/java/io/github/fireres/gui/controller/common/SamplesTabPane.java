package io.github.fireres.gui.controller.common;

import io.github.fireres.gui.annotation.FxmlView;
import io.github.fireres.gui.annotation.Initialize;
import io.github.fireres.gui.controller.AbstractComponent;
import io.github.fireres.gui.initializer.general.SamplesInitializer;
import io.github.fireres.gui.service.SampleService;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@FxmlView("samplesTabPane.fxml")
@RequiredArgsConstructor
@Component
@Initialize(SamplesInitializer.class)
public class SamplesTabPane extends AbstractComponent<TabPane> {

    private final SampleService sampleService;

    @FXML
    public void addSample(Event event) {
        if (getComponent().getTabs().size() == 1) {
            return;
        }

        log.info("Add sample tab pressed");

        val target = (Tab) event.getTarget();

        if (isAddSampleTab(target)) {
            sampleService.createNewSample(this);

            if (getComponent().getTabs().size() > 2) {
                getComponent().setTabClosingPolicy(TabPane.TabClosingPolicy.SELECTED_TAB);
            }
        }
    }

    private boolean isAddSampleTab(Tab t2) {
        return t2 != null && t2.getId() != null && t2.getId().equals("addSampleTab");
    }

    public List<SampleTab> getSampleTabs() {
        return getChildren(SampleTab.class);
    }

}

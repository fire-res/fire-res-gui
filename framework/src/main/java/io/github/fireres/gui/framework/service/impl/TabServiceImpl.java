package io.github.fireres.gui.framework.service.impl;

import io.github.fireres.gui.framework.service.TabService;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class TabServiceImpl implements TabService {

    private final Map<String, Integer> tabOrder;

    @Override
    public void disableTab(Tab tab) {
        tab.setDisable(true);

        if (tab.getTabPane() != null) {
            tab.getTabPane().getTabs().removeIf(tab::equals);
        }
    }

    @Override
    public void enableTab(Tab tab, TabPane tabPane) {
        tab.setDisable(false);
        tabPane.getTabs().add(tab);
        tabPane.getTabs().sort(Comparator.comparing(t -> tabOrder.get(t.getText())));
    }

}

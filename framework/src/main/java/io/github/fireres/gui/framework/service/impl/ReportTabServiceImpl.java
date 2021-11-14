package io.github.fireres.gui.framework.service.impl;

import io.github.fireres.gui.framework.service.ReportTabService;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ReportTabServiceImpl implements ReportTabService {

    private final Map<String, Integer> tabOrder;

    @Override
    public void disableTab(Tab reportTab) {
        reportTab.setDisable(true);

        if (reportTab.getTabPane() != null) {
            reportTab.getTabPane().getTabs().removeIf(reportTab::equals);
        }
    }

    @Override
    public void enableTab(Tab reportTab, TabPane reportTabPane) {
        reportTab.setDisable(false);
        reportTabPane.getTabs().add(reportTab);
        sortTabs(reportTabPane);
    }

    @Override
    public void sortTabs(TabPane reportTabPane) {
        val tabs = new ArrayList<>(reportTabPane.getTabs());

        tabs.sort(Comparator.comparing(t -> tabOrder.get(t.getText())));

        reportTabPane.getTabs().clear();
        reportTabPane.getTabs().setAll(tabs);
    }

}

package io.github.fireres.gui.framework.service;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public interface ReportTabService {

    void disableTab(Tab reportTab);

    void enableTab(Tab reportTab, TabPane reportTabPane);

    void sortTabs(TabPane reportTabPane);

}

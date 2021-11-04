package io.github.fireres.gui.framework.service;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public interface TabService {

    void disableTab(Tab tab);

    void enableTab(Tab tab, TabPane tabPane);

}

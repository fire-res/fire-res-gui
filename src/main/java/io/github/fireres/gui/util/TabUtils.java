package io.github.fireres.gui.util;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import lombok.experimental.UtilityClass;

import java.util.Comparator;
import java.util.Map;

import static io.github.fireres.gui.model.ElementIds.EXCESS_PRESSURE_TAB;
import static io.github.fireres.gui.model.ElementIds.FIRE_MOD_TAB;
import static io.github.fireres.gui.model.ElementIds.HEAT_FLOW_TAB;
import static io.github.fireres.gui.model.ElementIds.UNHEATED_SURFACE_TAB;

@UtilityClass
public class TabUtils {

    private final static Map<String, Integer> TAB_ORDER = Map.of(
            FIRE_MOD_TAB, 0,
            EXCESS_PRESSURE_TAB, 1,
            HEAT_FLOW_TAB, 2,
            UNHEATED_SURFACE_TAB, 3
    );

    public static void disableTab(Tab tab) {
        tab.setDisable(true);

        if (tab.getTabPane() != null) {
            tab.getTabPane().getTabs().removeIf(tab::equals);
        }
    }

    public static void enableTab(Tab tab, TabPane reportsTabPane) {
        tab.setDisable(false);
        reportsTabPane.getTabs().add(tab);
        reportsTabPane.getTabs().sort(Comparator.comparing(t -> TAB_ORDER.get(t.getId())));
    }

}

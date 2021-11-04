package io.github.fireres.gui.framework.service;

import io.github.fireres.core.model.Report;
import javafx.scene.control.TableView;

import java.util.Map;

public interface DataViewService {

    TableView<Map<String, Number>> getDataViewer(Report<?> report);

}

package io.github.fireres.gui.framework.service;

import io.github.fireres.core.model.Sample;

import java.nio.file.Path;
import java.util.List;

public interface ExportService {

    void exportReports(Path path, String filename, List<Sample> samples);

}

package io.github.fireres.gui.service;

import io.github.fireres.core.model.Sample;
import io.github.fireres.excel.ReportConstructor;
import io.github.fireres.gui.framework.service.ExportService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExportServiceImpl implements ExportService {

    private final ReportConstructor reportConstructor;

    @Override
    public void exportReports(Path path, String filename, List<Sample> samples) {
        val outputFile = path.resolve(filename + ".xlsx").toFile();

        reportConstructor.construct(samples, outputFile);
    }

}

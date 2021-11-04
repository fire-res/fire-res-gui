package io.github.fireres.gui.service;

import io.github.fireres.core.model.Report;
import io.github.fireres.core.model.ReportType;
import io.github.fireres.excel.report.ExcelReportsBuilder;
import io.github.fireres.gui.component.DataViewer;
import io.github.fireres.gui.framework.service.DataViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

import static java.util.Collections.singletonList;

@Component
@RequiredArgsConstructor
public class DataViewServiceImpl implements DataViewService {

    private final Map<ReportType, ExcelReportsBuilder> reportBuilders;

    @Override
    public DataViewer getDataViewer(Report<?> report) {
        return new DataViewer(
                reportBuilders.get(report.getType())
                        .build(singletonList(report))
                        .get(0)
        );
    }

}

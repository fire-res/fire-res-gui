package io.github.fireres.gui.service;

import io.github.fireres.core.model.Report;
import io.github.fireres.core.model.ReportType;
import io.github.fireres.excel.core.builder.ExcelReportsBuilder;
import io.github.fireres.gui.component.DataViewer;
import io.github.fireres.gui.framework.service.DataViewService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.Collections.singletonList;

@Component
@RequiredArgsConstructor
public class DataViewServiceImpl implements DataViewService {

    private final List<ExcelReportsBuilder> reportsBuilders;

    @Override
    public DataViewer getDataViewer(Report<?> report) {
        val reportBuilder = lookupReportBuilder(report.getType());

        return new DataViewer(reportBuilder.build(singletonList(report)).get(0));
    }

    private ExcelReportsBuilder lookupReportBuilder(ReportType reportType) {
        return reportsBuilders.stream()
                .filter(builder -> builder.supportedReportType().equals(reportType))
                .findFirst()
                .orElseThrow();
    }

}

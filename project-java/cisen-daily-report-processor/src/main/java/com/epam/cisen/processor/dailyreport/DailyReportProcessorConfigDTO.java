package com.epam.cisen.processor.dailyreport;

import com.epam.cisen.core.api.dto.ConfigDTO;

public class DailyReportProcessorConfigDTO extends ConfigDTO {

    public DailyReportProcessorConfigDTO() {
        super("dailyReportProcessor", ConfigDTO.BaseType.PROCESSOR);
    }
}

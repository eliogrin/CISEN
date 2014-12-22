package com.epam.cisen.processor.dailyreport;

import com.epam.cisen.core.api.dto.ConfigDTO;

public class DailyReportProcessorConfigDTO extends ConfigDTO {

    public DailyReportProcessorConfigDTO() {
        super("Daily Report", ConfigDTO.BaseType.PROCESSOR);
    }
}

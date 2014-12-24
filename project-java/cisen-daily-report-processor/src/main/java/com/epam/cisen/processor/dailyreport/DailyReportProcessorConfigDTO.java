package com.epam.cisen.processor.dailyreport;

import com.epam.cisen.core.api.dto.ConfigDTO;

public class DailyReportProcessorConfigDTO extends ConfigDTO {

    private String description;

    public DailyReportProcessorConfigDTO() {
        super("Daily Report", ConfigDTO.BaseType.PROCESSOR);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

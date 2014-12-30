package com.epam.cisen.processor.dailyreport;

import com.epam.cisen.core.api.dto.ConfigDTO;

public class DailyReportProcessorConfigDTO extends ConfigDTO {
//
//    private String description;

    public DailyReportProcessorConfigDTO() {
        super("Daily Report", ConfigDTO.BaseType.PROCESSOR,
                "This report run once a day and collect information about last twenty-four hours activity on CI.");
    }

//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
}

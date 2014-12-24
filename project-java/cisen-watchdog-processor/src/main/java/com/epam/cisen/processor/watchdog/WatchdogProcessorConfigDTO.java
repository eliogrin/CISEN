package com.epam.cisen.processor.watchdog;

import com.epam.cisen.core.api.dto.ConfigDTO;

public class WatchdogProcessorConfigDTO extends ConfigDTO {

    private String minutes;

    private String description;

    public WatchdogProcessorConfigDTO() {
        super("Watchdog Processor", ConfigDTO.BaseType.PROCESSOR);
    }

    public String getTimeout() {
        return minutes;
    }

    public void setTimeout(String minutes) {
        this.minutes = minutes;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

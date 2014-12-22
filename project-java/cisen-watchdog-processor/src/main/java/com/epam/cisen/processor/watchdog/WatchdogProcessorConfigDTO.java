package com.epam.cisen.processor.watchdog;

import com.epam.cisen.core.api.dto.ConfigDTO;

public class WatchdogProcessorConfigDTO extends ConfigDTO {

    private String minutes;

    public WatchdogProcessorConfigDTO() {
        super("Watchdog Processor", ConfigDTO.BaseType.PROCESSOR);
    }

    public String getTimeout() {
        return minutes;
    }

    public void setTimeout(String minutes) {
        this.minutes = minutes;
    }
}

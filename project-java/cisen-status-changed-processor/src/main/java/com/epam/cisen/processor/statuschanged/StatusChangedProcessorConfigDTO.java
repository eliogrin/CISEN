package com.epam.cisen.processor.statuschanged;

import com.epam.cisen.core.api.dto.ConfigDTO;

public class StatusChangedProcessorConfigDTO extends ConfigDTO {

    boolean fromFailToGreen = true;
    boolean fromFailToFail = true;
    boolean fromGreenToFail = true;

    public StatusChangedProcessorConfigDTO() {
        this("Status Changer", BaseType.PROCESSOR);
    }

    public StatusChangedProcessorConfigDTO(String configTableName, BaseType messenger) {
        super(configTableName, messenger);
    }

    public boolean isFromFailToGreen() {
        return fromFailToGreen;
    }

    public void setFromFailToGreen(boolean fromFailToGreen) {
        this.fromFailToGreen = fromFailToGreen;
    }

    public boolean isFromFailToFail() {
        return fromFailToFail;
    }

    public void setFromFailToFail(boolean fromFailToFail) {
        this.fromFailToFail = fromFailToFail;
    }

    public boolean isFromGreenToFail() {
        return fromGreenToFail;
    }

    public void setFromGreenToFail(boolean fromGreenToFail) {
        this.fromGreenToFail = fromGreenToFail;
    }

}

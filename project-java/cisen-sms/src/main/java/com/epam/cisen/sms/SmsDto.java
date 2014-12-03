package com.epam.cisen.sms;

import com.epam.cisen.core.api.dto.ConfigDTO;

public class SmsDto extends ConfigDTO {

    private String recipient = null;

    public SmsDto() {
        super("sms", BaseType.MESSENGER);
    }

    public void setRecipient(final String recipient) {
        this.recipient = recipient;
    }

    public String getRecipient() {
        return this.recipient;
    }
}

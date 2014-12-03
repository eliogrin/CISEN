package com.epam.cisen.email;

import com.epam.cisen.core.api.dto.ConfigDTO;

public class EmailConfigDTO extends ConfigDTO {

    private String recipient = "Input your e-mail.";

    public EmailConfigDTO() {
        super("EmailMessenger", BaseType.MESSENGER);
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

}

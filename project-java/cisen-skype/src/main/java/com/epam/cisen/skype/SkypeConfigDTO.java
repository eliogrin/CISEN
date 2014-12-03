package com.epam.cisen.skype;

import com.epam.cisen.core.api.dto.ConfigDTO;

public class SkypeConfigDTO extends ConfigDTO {

    public static enum RECIPIENT_TYPE {
        USER,
        CHAT
    }

    private String recipient = "Set you Skype id or blob id of the group.";
    private RECIPIENT_TYPE recipient_type = RECIPIENT_TYPE.USER;
    private boolean updateChatName = false;

    public SkypeConfigDTO() {
        super("SkypeMessenger", ConfigDTO.BaseType.MESSENGER);
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public RECIPIENT_TYPE getRecipient_type() {
        return recipient_type;
    }

    public void setRecipient_type(RECIPIENT_TYPE recipient_type) {
        this.recipient_type = recipient_type;
    }

    public boolean isUpdateChatName() {
        return updateChatName;
    }

    public void setUpdateChatName(boolean updateChatName) {
        this.updateChatName = updateChatName;
    }
}

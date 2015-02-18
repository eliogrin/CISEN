package com.epam.cisen.core.api.dto;

import org.jongo.marshall.jackson.oid.Id;
import org.jongo.marshall.jackson.oid.ObjectId;

public class CIInitializer {

    @Id
    @ObjectId
    private String id;
    private String userId;
    private Action action;

    public CIInitializer() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public static enum Action {
        RUN,
        PUSH
    }
}

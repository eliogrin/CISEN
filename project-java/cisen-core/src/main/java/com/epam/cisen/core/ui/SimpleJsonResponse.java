package com.epam.cisen.core.ui;

public class SimpleJsonResponse {

    private String name;
    private String value;

    public SimpleJsonResponse(String myName, String myValue) {
        this.name = myName;
        this.value = myValue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

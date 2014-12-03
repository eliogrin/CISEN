package com.epam.cisen.core.api.dto;

import org.jongo.marshall.jackson.oid.Id;

import java.util.UUID;

public class CiJob {

    @Id
    private String id;
    private String name;
    private String ci;
    private String messenger;
    private String processor;

    public CiJob() {
        this(UUID.randomUUID().toString());
    }

    public CiJob(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCi() {
        return ci;
    }

    public void setCi(String ci) {
        this.ci = ci;
    }

    public String getMessenger() {
        return messenger;
    }

    public void setMessenger(String messenger) {
        this.messenger = messenger;
    }

    public String getProcessor() {
        return processor;
    }

    public void setProcessor(String processor) {
        this.processor = processor;
    }
}

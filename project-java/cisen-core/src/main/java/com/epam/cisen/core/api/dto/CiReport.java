package com.epam.cisen.core.api.dto;

import org.jongo.marshall.jackson.oid.Id;
import org.jongo.marshall.jackson.oid.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class CiReport implements Cloneable {

    @Id
    @ObjectId
    private String id;
    private String jobId;
    private String systemId;
    private String buildId;
    private String buildNumber;
    private Status status;
    private String url;
    private String textStatus;
    private long startTime;
    private long duration;
    private List<CIInitializer> initializers;
    private boolean processed;

    public CiReport() {
    }


    public CiReport clone() throws CloneNotSupportedException {
        CiReport clone = (CiReport) super.clone();
        clone.setId(null);
        return clone;
    }

    public void addInitializer(CIInitializer initializer) {
        if (initializers == null) {
            initializers = new ArrayList<>();
        }
        initializers.add(initializer);
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public String getBuildId() {
        return buildId;
    }

    public void setBuildId(String buildId) {
        this.buildId = buildId;
    }

    public String getBuildNumber() {
        return buildNumber;
    }

    public void setBuildNumber(String buildNumber) {
        this.buildNumber = buildNumber;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTextStatus() {
        return textStatus;
    }

    public void setTextStatus(String textStatus) {
        this.textStatus = textStatus;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public List<CIInitializer> getInitializers() {
        return initializers;
    }

    public void setInitializers(List<CIInitializer> initializers) {
        this.initializers = initializers;
    }

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    public static enum Status {
        GREEN,
        RED,
        YELLOW,
        IN_PROGRESS;

        public boolean isSuccess() {
            return this.equals(GREEN);
        }

        public boolean inProgress() {
            return this.equals(IN_PROGRESS);
        }
    }
}

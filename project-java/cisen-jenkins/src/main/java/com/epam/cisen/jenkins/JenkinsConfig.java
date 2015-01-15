package com.epam.cisen.jenkins;

import com.epam.cisen.core.api.dto.ConfigDTO;

public class JenkinsConfig extends ConfigDTO {

    private String baseURL;
    private String login;
    private String pass;
    private String jobName;

    public JenkinsConfig() {
        super("Jenkins", BaseType.CI);
    }

    public JenkinsConfig(String baseURL, String login, String pass, String jobName) {
        this();
        this.baseURL = baseURL;
        this.login = login;
        this.pass = pass;
        this.jobName = jobName;
    }

    public String getBaseURL() {
        return baseURL;
    }

    public void setBaseURL(String baseURL) {
        this.baseURL = baseURL;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }
}

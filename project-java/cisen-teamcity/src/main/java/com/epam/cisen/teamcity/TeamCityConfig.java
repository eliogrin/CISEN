package com.epam.cisen.teamcity;

import com.epam.cisen.core.api.dto.ConfigDTO;

/**
 * Created by Vladislav on 19.11.2014.
 */
public class TeamCityConfig extends ConfigDTO {
    private String baseURL;
    private String login;
    private String pass;
    private String buildTypeId;

    public TeamCityConfig() {
        super("TeamCity", BaseType.CI);
    }

    public TeamCityConfig(String baseURL, String login, String pass, String buildTypeId) {
        this();
        this.baseURL = baseURL;
        this.login = login;
        this.pass = pass;
        this.buildTypeId = buildTypeId;
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

    public String getBuildTypeId() {
        return buildTypeId;
    }

    public void setBuildTypeId(String buildTypeId) {
        this.buildTypeId = buildTypeId;
    }
}

package com.epam.cisen.jenkins.connector;

import java.io.IOException;

import com.epam.cisen.core.connector.BaseConnection;
import com.epam.cisen.jenkins.JenkinsConfig;

public class JenkinsConnection extends BaseConnection {

    private static final String BUILD = "/job/%s/api/xml";
    private static final String LAST_BUILD = "/job/%s/lastBuild/api/xml";

    public JenkinsConnection(JenkinsConfig config) {
        super(config.getBaseURL(), config.getLogin(), config.getPass());
    }

    public JenkinsConnection(String baseAddress) {
        super(baseAddress);
    }

    public JenkinsConnection(String baseAddress, String login, String pass) {
        super(baseAddress, login, pass);
    }

    public String readAllBuilds(String buildTypeId) throws IOException {
        return read(BUILD, buildTypeId);
    }

    public String readBuildInfo(String id) throws IOException {
        return read(LAST_BUILD, id);
    }
}

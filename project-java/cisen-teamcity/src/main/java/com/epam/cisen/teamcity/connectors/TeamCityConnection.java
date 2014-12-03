package com.epam.cisen.teamcity.connectors;

import com.epam.cisen.teamcity.TeamCityConfig;

import java.io.IOException;

/**
 * Created by Vladislav on 19.11.2014.
 */
public class TeamCityConnection extends BaseConnection {

    public static final String ALL_BUILDS = "/httpAuth/app/rest/buildTypes/id:%s/builds";
    public static final String BUILD_INFO = "/httpAuth/app/rest/builds/id:%s";

    public TeamCityConnection(TeamCityConfig config){
        super(config.getBaseURL(), config.getLogin(), config.getPass());
    }

    public TeamCityConnection(String baseAddress) {
        super(baseAddress);
    }

    public TeamCityConnection(String baseAddress, String login, String pass) {
        super(baseAddress, login, pass);
    }

    public String readAllBuilds(String buildTypeId) throws IOException {
        return read(ALL_BUILDS, buildTypeId);
    }

    public String readBuildInfo(String id) throws IOException {
        return read(BUILD_INFO, id);
    }
}

package com.epam.cisen.teamcity;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.osgi.service.component.ComponentContext;

import com.epam.cisen.core.api.AbstractConnector;
import com.epam.cisen.core.api.Connector;
import com.epam.cisen.core.api.dto.CiReport;

@Component
@Service(Connector.class)
public class TeamCityConnector extends AbstractConnector<TeamCityConfig> {

    private static final TeamCityConfig CONFIG = new TeamCityConfig();

    static {
        CONFIG.setBaseURL("URL");
        CONFIG.setBuildTypeId("Build ID");
        CONFIG.setLogin("Login");
        CONFIG.setPass("Password");
    }

    private final TeamCityWorker worker = new TeamCityWorker();

    @Override
    public TeamCityConfig getPluginTemplateConfig() {
        return CONFIG;
    }

    @Override
    protected CiReport check(TeamCityConfig config) throws Exception {
        return worker.checkStatus(config);
    }

    @Override
    protected String getBuildKey(TeamCityConfig config) {
        return config.getBaseURL() + "|" + config.getBuildTypeId();
    }

    protected void activatePlugin(ComponentContext componentContext) {

    }

    protected TeamCityConfig create(String buildTypeIs) {
        TeamCityConfig config = new TeamCityConfig();
        config.setBuildTypeId(buildTypeIs);
        // TODO:set base url, login and pass.
        return config;
    }
}

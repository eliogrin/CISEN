package com.epam.cisen.teamcity;

import java.util.ArrayList;
import java.util.List;

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
        CONFIG.setBaseURL("Base TeamCity url");
        CONFIG.setBuildTypeId("Build id in TC");
        CONFIG.setLogin("TC user's login");
        CONFIG.setPass("TC user's pass");
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

//    @Override
//    protected List<TeamCityConfig> getTestData() {
//        List<TeamCityConfig> result = new ArrayList<>();
//        result.add(create("RandomBuild_RandomBuild_Build"));
//        return result;
//    }

    @Override
    protected void activatePlugin(ComponentContext componentContext) {

    }

    protected TeamCityConfig create(String buildTypeIs) {
        TeamCityConfig config = new TeamCityConfig();
        config.setBuildTypeId(buildTypeIs);
        // TODO:set base url, login and pass.
        return config;
    }
}

package com.epam.cisen.jenkins;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.osgi.service.component.ComponentContext;

import com.epam.cisen.core.api.AbstractConnector;
import com.epam.cisen.core.api.Connector;
import com.epam.cisen.core.api.dto.CiReport;

@Component
@Service(Connector.class)
public class JenkinsConnector extends AbstractConnector<JenkinsConfig> {

    private static final JenkinsConfig CONFIG = new JenkinsConfig();

    static {
        CONFIG.setBaseURL("URL");
        CONFIG.setJobName("Job name");
        CONFIG.setLogin("Login");
        CONFIG.setPass("Password");
    }

    private final JenkinsWorker worker = new JenkinsWorker();

    @Override
    public JenkinsConfig getPluginTemplateConfig() {
        return CONFIG;
    }

    @Override
    protected void activatePlugin(ComponentContext componentContext) {

    }

    @Override
    protected CiReport check(JenkinsConfig config) {
        return worker.checkStatus(config);
    }

    @Override
    protected String getBuildKey(JenkinsConfig config) {
        return config.getBaseURL() + "|" + config.getJobName();
    }
}

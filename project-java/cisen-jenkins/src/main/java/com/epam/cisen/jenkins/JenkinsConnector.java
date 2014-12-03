package com.epam.cisen.jenkins;

import com.epam.cisen.core.api.AbstractConnector;
import com.epam.cisen.core.api.Connector;
import com.epam.cisen.core.api.dto.CiReport;
import com.epam.cisen.core.api.dto.ConfigDTO;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;

@Component
@Service(Connector.class)
public class JenkinsConnector extends AbstractConnector<ConfigDTO> {

    @Override
    public ConfigDTO getPluginTemplateConfig() {
        return new ConfigDTO("Jenkins", ConfigDTO.BaseType.CI);
    }

    @Override
    protected CiReport check(ConfigDTO config) {
        return null;
    }

    @Override
    protected String getBuildKey(ConfigDTO config) {
        return null;
    }
}

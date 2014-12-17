package com.epam.cisen.processor.log4j;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
@Service(Log4jConfigurationService.class)
public class Log4jConfigurationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(Log4jConfigurationService.class);

    @Reference
    private ConfigurationAdmin configurationAdmin;

    @Activate
    public void start(Map<String, Object> properties) {

        try {
            final Configuration configuration = configurationAdmin.getConfiguration("org.ops4j.pax.logging", null);

            final Hashtable<String, Object> log4jProps = new Hashtable<>();
            log4jProps.put("log4j.rootLogger", "ALL, file");
            log4jProps.put("log4j.appender.file", "org.apache.log4j.RollingFileAppender");
            log4jProps.put("log4j.appender.file.File", "../../../logs/error.log");
            log4jProps.put("log4j.appender.file.MaxFileSize", "1MB");
            log4jProps.put("log4j.appender.file.MaxBackupIndex", "1");
            log4jProps.put("log4j.appender.file.layout", "org.apache.log4j.PatternLayout");
            log4jProps.put("log4j.appender.file.layout.ConversionPattern", "%d{ABSOLUTE} %5p %c{1}:%L - %m%n");
            configuration.update(log4jProps);

        } catch (IOException ex) {
            LOGGER.error("Fail to load configuration.", ex);
        }
    }

}
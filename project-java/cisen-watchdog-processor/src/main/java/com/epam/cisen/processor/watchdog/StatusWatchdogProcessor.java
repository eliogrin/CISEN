package com.epam.cisen.processor.watchdog;

import com.epam.cisen.core.api.AbstractProcessor;
import com.epam.cisen.core.api.Processor;
import com.epam.cisen.core.api.dto.CiReport;
import com.epam.cisen.core.api.dto.ConfigDTO;
import com.epam.cisen.core.api.dto.Constants;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;
import org.osgi.service.component.ComponentContext;

import java.io.IOException;
import java.util.Map;

@Component
@Service(Processor.class)
public class StatusWatchdogProcessor extends AbstractProcessor<WatchdogProcessorConfigDTO> {

    private final static String SUBJECT = "Frozen build detected";
    private final static String BODY = "It looks like current build is in progress from an ancient times.";

    @Override
    protected WatchdogProcessorConfigDTO getPluginTemplateConfig() {
        WatchdogProcessorConfigDTO templateConfig = new WatchdogProcessorConfigDTO();
        templateConfig.setTimeout("Set watchdog interval in minutes");
        return templateConfig;
    }

    @Override
    protected void activatePlugin(ComponentContext componentContext) {

    }

    @Override
    public void process() {
        MongoCollection builds = mongoDBService.getCollection(Constants.DB.BUILDS);
        for (Map.Entry<String, WatchdogProcessorConfigDTO> entry : getJobs().entrySet()) {
            long boundTime = System.currentTimeMillis() - Integer.parseInt(entry.getValue().getTimeout()) * 60000;
            String queryToSend = String.format("{jobId:'%s', processed:'false', startTime:{$lt: '%s'}, status: '%s'}", entry.getKey(), boundTime, CiReport.Status.IN_PROGRESS);
            try {
                MongoCursor<CiReport> ciReports = builds.find(queryToSend).as(CiReport.class);
                if (ciReports.count() > 0) {
                    putMessage(entry.getKey(), SUBJECT, BODY);
                }
                markReportsAsProcessed(builds, ciReports);
                ciReports.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void markReportsAsProcessed(MongoCollection builds, MongoCursor<CiReport> ciReports) {
        while (ciReports.hasNext()) {
            CiReport report = ciReports.next();
            report.setProcessed(true);
            builds.update(String.format("{id:'%s'}", report.getId()), report);
        }
    }
}

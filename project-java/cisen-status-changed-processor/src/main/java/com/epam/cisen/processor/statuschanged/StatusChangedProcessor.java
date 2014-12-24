package com.epam.cisen.processor.statuschanged;

import com.epam.cisen.core.api.AbstractProcessor;
import com.epam.cisen.core.api.Processor;
import com.epam.cisen.core.api.dto.CIInitializer;
import com.epam.cisen.core.api.dto.CiReport;
import com.epam.cisen.core.api.dto.Constants;

import org.apache.commons.lang.StringUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;
import org.osgi.service.component.ComponentContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@Service(Processor.class)
public class StatusChangedProcessor extends AbstractProcessor<StatusChangedProcessorConfigDTO> {

    private static final StatusChangedProcessorConfigDTO CONFIG = new StatusChangedProcessorConfigDTO();

    static {
        CONFIG.setFromFailToFail(true);
        CONFIG.setFromFailToGreen(true);
        CONFIG.setFromGreenToFail(true);
    }

    @Override
    protected StatusChangedProcessorConfigDTO getPluginTemplateConfig() {
        return CONFIG;
    }

    @Override
    protected void activatePlugin(ComponentContext componentContext) {

    }

    @Override
    public void process() {
        Map<String, StatusChangedProcessorConfigDTO> jobs = getJobs();

        MongoCollection collection = mongoDBService.getCollection(Constants.DB.BUILDS);
        for (Map.Entry<String, StatusChangedProcessorConfigDTO> entry : jobs.entrySet()) {
            String toSendQuery = "{jobId:'" + entry.getKey() + "', processed:'false'}";
            try {
                MongoCursor<CiReport> ciReports = collection.find(toSendQuery).as(CiReport.class);
                int count = ciReports.count();
                CiReport firstReport = null;
                for (int i = 0; i < count - 1; i++) {
                    CiReport report = ciReports.next();
                    if (firstReport == null) {
                        firstReport = report;
                    }
                    report.setProcessed(true);
                    collection.update("{id:'" + report.getId() + "'}", report);
                }
                if (ciReports.hasNext()) {
                    CiReport lastReport = ciReports.next();
                    CiReport.Status firstStatus = firstReport.getStatus();
                    generateMessage(entry.getKey(), entry.getValue(), firstStatus, lastReport);

                }
                ciReports.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void generateMessage(String jobId, StatusChangedProcessorConfigDTO config, CiReport.Status firstStatus, CiReport lastReport) {
        CiReport.Status lastStatus = lastReport.getStatus();
        if (lastStatus.inProgress()) {
            return;
        }
        if (firstStatus.isSuccess() && lastStatus.isSuccess()) {
            return;
        }

        String users = collectUsers(lastReport);

        if (config.fromGreenToFail && firstStatus.isSuccess()) {

            String message = String.format("(rain) - build #%s was FAILURE after push of %s", lastReport.getBuildNumber(), users);

            putMessage(jobId, "Build failed", message);
        } else if (config.fromFailToGreen && lastStatus.isSuccess()) {

            String message = String.format("(sun)- build #%s was SUCCESS special thanks to %s", lastReport.getBuildNumber(), users);

            putMessage(jobId, "Build green", message);
        } else if (config.fromFailToFail && !firstStatus.isSuccess() && !lastStatus.isSuccess()) {

            String message = String.format("(tumbleweed) - sorry, but build #%s still FAILURE after push of %s", lastReport.getBuildNumber(), users);

            putMessage(jobId, "Build still failed", message);
        }
    }

    private String collectUsers(CiReport lastReport) {
        List<String> users = new ArrayList<>();

        for (CIInitializer user : lastReport.getInitializers()) {
            users.add(user.getUserId());
        }
        return StringUtils.join(users, ", ");
    }

}

package com.epam.cisen.core.api;

import java.util.Map;

import org.apache.felix.scr.annotations.Component;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.cisen.core.api.dto.CiReport;
import com.epam.cisen.core.api.dto.ConfigDTO;
import com.epam.cisen.core.api.dto.Constants;
import com.google.common.collect.Maps;

@Component(componentAbstract = true)
public abstract class AbstractConnector<T extends ConfigDTO> extends AbstractPlugin<T> implements Connector {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractConnector.class);

    @Override
    protected Constants.DB getTemplateTableName() {
        return Constants.DB.CI_PLUGINS;
    }

    @Override
    protected Constants.DB getConfigTableName() {
        return Constants.DB.CI_CONFIGS;
    }

    /**
     * Get build unique key.
     *
     * @param config
     * @return
     */
    protected abstract String getBuildKey(T config);

    @Override
    public void check() {
        MongoCollection collection = mongoDBService.getCollection(Constants.DB.BUILDS);

        Map<String, CiReport> resultsMap = Maps.newHashMap();

        for (Map.Entry<String, T> entry : getJobs().entrySet()) {
            // Get build unique key
            String key = getBuildKey(entry.getValue());
            try {
                // If build already checked
                CiReport report = resultsMap.get(key);
                if (report != null) {
                    // Clone build
                    report = report.clone();
                } else {
                    // Else check build info
                    report = check(entry.getValue());
                    resultsMap.put(key, report);
                }
                // Set job id and add to reports list.
                report.setJobId(entry.getKey());
                if (buildInDB(collection, report.getJobId(), report.getBuildId())) {
                    collection.insert(report);
                }
            } catch (Exception ex) {
                LOGGER.error("Cannot check build.", ex);
            }
        }
    }

    private boolean buildInDB(MongoCollection collection, String jobId, String buildId) {
        try {
            MongoCursor cursor = collection.find("{ $and [ {jobId:#}, {buildId:#} ] }", jobId, buildId).as(
                    CiReport.class);
            try {
                return !cursor.hasNext();
            } finally {
                cursor.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    protected abstract CiReport check(T config) throws Exception;

}

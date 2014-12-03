package com.epam.cisen.teamcity;

import com.epam.cisen.core.api.dto.CIInitializer;
import com.epam.cisen.core.api.dto.CiReport;
import com.epam.cisen.teamcity.connectors.TeamCityConnection;
import com.epam.cisen.teamcity.parsers.BaseXMLParser;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Vladislav on 19.11.2014.
 */
public class TeamCityWorker {

    private static final String LAST_BUILD_XPATH = "/builds/build[1]";
//    public static final String CI_CONFIGS = "ci_configs";

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss");
//    private final MongoCollection collection;

//    public TeamCityWorker(Jongo jongo) {
//        this.collection = jongo.getCollection(CI_CONFIGS);
//    }

//    public List<CiReport> parsCI() throws Exception {
//        //Read all TeamCity configs
//        List<TeamCityConfig> configurations = readJobConfigs();
//        //Create map to ignore duplicate configurations
//        Map<String, CiReport> resultMap = new HashMap<>();
//        List<CiReport> result = new ArrayList<>(configurations.size());
//        for (TeamCityConfig config : configurations) {
//            //Key for result map
//            String configKey = config.getBaseURL() + "|" + config.getBuildTypeId();
//            //If config already processed
//            CiReport report = resultMap.get(configKey);
//            if (report != null) {
//                result.add(report);
//                continue;
//            }
//            //If new configuration
//            //Read data from TC
//            report = checkStatus(config);
//            //Add to result collections
//            resultMap.put(configKey, report);
//            result.add(report);
//        }
//        return result;
//    }
//
//    private List<TeamCityConfig> readJobConfigs() throws IOException {
//        MongoCursor<TeamCityConfig> cursor = collection.find().as(TeamCityConfig.class);
//        List<TeamCityConfig> result = new ArrayList<>(cursor.count());
//        try {
//            while (cursor.hasNext()) {
//                result.add(cursor.next());
//            }
//        } finally {
//            cursor.close();
//        }
//        return result;
//    }

    public CiReport checkStatus(TeamCityConfig config) throws Exception {
        TeamCityConnection connection = new TeamCityConnection(config);

        String allBuildsXML = connection.readAllBuilds(config.getBuildTypeId());
        BaseXMLParser allBuildsParser = new BaseXMLParser(allBuildsXML);
        String lastBuildId = allBuildsParser.getAttribute(LAST_BUILD_XPATH, "id");

        String lastBuildXML = connection.readBuildInfo(lastBuildId);
        BaseXMLParser lastBuildParser = new BaseXMLParser(lastBuildXML);

        return toReport(lastBuildParser);
    }

    private CiReport toReport(BaseXMLParser build) throws Exception {
        CiReport report = new CiReport();
        report.setSystemId(build.getAttribute("/build", "buildTypeId"));
        report.setBuildId(build.getAttribute("/build", "id"));
        report.setBuildNumber(build.getAttribute("/build", "number"));
        report.setStatus(toStatus(build.getAttribute("/build", "status")));
        report.setUrl(build.getAttribute("/build", "webUrl"));
        report.setTextStatus(build.get("/build/statusText"));
        report.setStartTime(toDate(build.get("/build/startDate")).getTime());
        long duration = toDate(build.get("/build/finishDate")).getTime() - report.getStartTime();
        report.setDuration(duration);
        if ("user".equals(build.getAttribute("/build/triggered", "type"))) {
            CIInitializer runner = new CIInitializer();
            runner.setAction(CIInitializer.Action.RUN);
            runner.setUserId(build.getAttribute("/build/triggered/user", "username"));
            report.addInitializer(runner);
        }
        List<String> changes = build.getAttributeList("/build/lastChanges//change", "username");
        for (String change : changes) {
            CIInitializer changer = new CIInitializer();
            changer.setAction(CIInitializer.Action.PUSH);
            changer.setUserId(change);
            report.addInitializer(changer);
        }
        return report;
    }

    private CiReport.Status toStatus(String teamcityStatus) {
        if ("SUCCESS".equals(teamcityStatus)) {
            return CiReport.Status.GREEN;
        }
        return CiReport.Status.RED;
    }

    private Date toDate(String teamcityDate) throws Exception {
        return simpleDateFormat.parse(teamcityDate);
    }
}

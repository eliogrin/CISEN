package com.epam.cisen.jenkins;

import java.text.SimpleDateFormat;

import com.epam.cisen.core.api.dto.CiReport;

public class JenkinsWorker {

    private static final String LAST_BUILD_XPATH = "/job/%s/lastBuild/api/json";

    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyyMMdd'T'HHmmss");

    public CiReport checkStatus(JenkinsConfig config) {
        // TeamCityConnection connection = new TeamCityConnection(config);
        //
        // String allBuildsXML = connection.readAllBuilds(config.getBuildTypeId());
        // BaseXMLParser allBuildsParser = new BaseXMLParser(allBuildsXML);
        // String lastBuildId = allBuildsParser.getAttribute(LAST_BUILD_XPATH, "id");
        //
        // String lastBuildXML = connection.readBuildInfo(lastBuildId);
        // BaseXMLParser lastBuildParser = new BaseXMLParser(lastBuildXML);

        return null; // toReport(lastBuildParser);
    }

    // private CiReport toReport(BaseXMLParser build) throws Exception {
    // CiReport report = new CiReport();
    // report.setSystemId(build.getAttribute("/build", "buildTypeId"));
    // report.setBuildId(build.getAttribute("/build", "id"));
    // report.setBuildNumber(build.getAttribute("/build", "number"));
    // report.setStatus(toStatus(build.getAttribute("/build", "status")));
    // report.setUrl(build.getAttribute("/build", "webUrl"));
    // report.setTextStatus(build.get("/build/statusText"));
    // report.setStartTime(toDate(build.get("/build/startDate")).getTime());
    // long duration = toDate(build.get("/build/finishDate")).getTime() - report.getStartTime();
    // report.setDuration(duration);
    // if ("user".equals(build.getAttribute("/build/triggered", "type"))) {
    // CIInitializer runner = new CIInitializer();
    // runner.setAction(CIInitializer.Action.RUN);
    // runner.setUserId(build.getAttribute("/build/triggered/user", "username"));
    // report.addInitializer(runner);
    // }
    // List<String> changes = build.getAttributeList("/build/lastChanges//change", "username");
    // for (String change : changes) {
    // CIInitializer changer = new CIInitializer();
    // changer.setAction(CIInitializer.Action.PUSH);
    // changer.setUserId(change);
    // report.addInitializer(changer);
    // }
    // return null;
    // }

    private CiReport.Status toStatus(String teamcityStatus) {
        if ("SUCCESS".equals(teamcityStatus)) {
            return CiReport.Status.GREEN;
        }
        return CiReport.Status.RED;
    }
}
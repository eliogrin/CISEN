package com.epam.cisen.jenkins;

import com.epam.cisen.core.api.dto.CIInitializer;
import com.epam.cisen.core.api.dto.CiReport;
import com.epam.cisen.core.connector.BaseXMLParser;
import com.epam.cisen.jenkins.connector.JenkinsConnection;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class JenkinsWorker {

    private static final String LAST_BUILD_XPATH = "/job/CISEN_buildflow/4/";

    public CiReport checkStatus(JenkinsConfig config) {

        JenkinsConnection connection = new JenkinsConnection(config);

        try {
            String lastBuildsXML = connection.readBuildInfo(config.getJobName());
            BaseXMLParser lastBuildParser = new BaseXMLParser(lastBuildsXML);
            return toReport(lastBuildParser);
        } catch (Exception e) {
            // TODO: add output into log
            System.out.printf(e.getMessage());
        }
        return null;
    }

    private CiReport toReport(BaseXMLParser build) throws Exception {
        CiReport report = new CiReport();

        report.setSystemId(build.get("/mavenModuleSetBuild/id"));
        //TODO: set property
        //report.setBuildId(build.getAttribute("/build", "id"));??
        report.setBuildNumber(build.get("/mavenModuleSetBuild/number"));
        report.setStatus(toStatus(build.get("/mavenModuleSetBuild/result")));
        report.setUrl(build.get("/mavenModuleSetBuild/url"));
        report.setTextStatus(build.get("/mavenModuleSetBuild/fullDisplayName"));

        report.setStartTime(Long.valueOf(build.get("/mavenModuleSetBuild/timestamp")));
        report.setDuration(Long.valueOf(build.get("/mavenModuleSetBuild/duration")));

        final NodeList userList = build.getNodeList("/mavenModuleSetBuild/culprit");

        for (int i = 0; i < userList.getLength(); i++) {
            final NodeList userData = userList.item(i).getChildNodes();
            for (int j = 0; j < userData.getLength(); j++) {
                final Node item = userData.item(j);
                if("fullName".equals(item.getNodeName())) {
                    CIInitializer runner = new CIInitializer();
                    runner.setAction(CIInitializer.Action.PUSH);
                    runner.setUserId(item.getTextContent());
                    report.addInitializer(runner);
                }
            }
        }

        return report;
    }

    private CiReport.Status toStatus(String status) {
        if ("SUCCESS".equalsIgnoreCase(status)) {
            return CiReport.Status.GREEN;
        }
        return CiReport.Status.RED;
    }
}

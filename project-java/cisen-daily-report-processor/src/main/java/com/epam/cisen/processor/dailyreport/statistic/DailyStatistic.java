package com.epam.cisen.processor.dailyreport.statistic;

import com.epam.cisen.core.api.dto.CIInitializer;
import com.epam.cisen.core.api.dto.CiReport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public class DailyStatistic {

    private static final String HEAD = "Yesterday from 00:00 to 23:59 we had %d builds, of which:\t";
    private static final String GREEN_PART = "(sun) Statistic: %d times and took %s\t";
    private static final String RED_PART = "(rain) Red: %d times and took %s\t";
    private static final String GREEN_PUSHER = "The title of 'Push Hero' gets %s for %d push in green build during the day.\t";
    private static final String RED_PUSHER = "(shielddeflect) The title of 'Red Pusher' gets %s for %d push in red build during the day.\t";
    private static final String BYE = "Have a nice day and green builds, Team.\t";

    private static final String HTML_HEAD = "<head></head><body><h1>c.i.s.e.n.</h1><div><p style=\"font-size:120%;\">"
        + "<b>Yesterday</b> from <i>00:00</i> to <i>23:59</i> we had <b>%d</b> builds of which:<br>";
    private static final String HTML_GREEN_PART = "&nbsp;<font color=\"green\"> Green: %d times and took %s.</font><br>";
    private static final String HTML_RED_PART = "&nbsp;<font color=\"red\"> Red: %d times and took %s.</font><br>";
    private static final String HTML_GREEN_PUSHER = "&nbsp;The title of <font color=\"green\">'Push Hero'</font> gets <font color=\"blue\">%s</font> for <b><i>%d</i></b> pushed green builds during the day.<br>";
    private static final String HTML_RED_PUSHER = "&nbsp;The title of <font color=\"red\">'Red Pusher'</font> gets <font color=\"blue\">%s</font> for <b><i>%d</i></b> push in red build during the day.<br>";
    private static final String HTML_BYE = "&nbsp;Have a nice day and green builds, Team.</p></div></body>";

    private Map<String, Integer> redPusherStatistic = new HashMap<>();
    private Map<String, Integer> greenPusherStatistic = new HashMap<>();

    private BuildStatistic redBuilds = new BuildStatistic();
    private BuildStatistic greenBuilds = new BuildStatistic();

    private int totalNumberOfBuilds = 0;


    public void putReportToStatistic(CiReport report) {
        totalNumberOfBuilds++;
        if (CiReport.Status.GREEN.equals(report.getStatus())) {
            greenBuilds.addReport(report);
            putGreenPushers(report.getInitializers());
        }
        if (CiReport.Status.RED.equals(report.getStatus())) {
            redBuilds.addReport(report);
            putRedPushers(report.getInitializers());
        }
    }

    private void putGreenPushers(List<CIInitializer> users) {
        for (CIInitializer user : users) {
            Integer numberOfPushes = greenPusherStatistic.get(user.getUserId());
            greenPusherStatistic.put(user.getUserId(), (numberOfPushes == null ? 1 : numberOfPushes + 1));
        }
    }

    private void putRedPushers(List<CIInitializer> users) {
        for (CIInitializer user : users) {
            Integer numberOfPushes = redPusherStatistic.get(user.getUserId());
            redPusherStatistic.put(user.getUserId(), (numberOfPushes == null ? 1 : numberOfPushes + 1));
        }
    }

    private static Map.Entry<String, Integer> getMostProductive(Map<String, Integer> statistic) {
        TreeSet<Integer> times = new TreeSet<>(statistic.values());
        if (!times.isEmpty()) {
            Integer higher = times.last();
            for (Map.Entry<String, Integer> entry : statistic.entrySet()) {
                if (higher.equals(entry.getValue())) {
                    return entry;
                }
            }
        }
        return null;
    }

    public String formatReport() {

        StringBuilder builder = new StringBuilder();
        builder.append(String.format(HEAD, totalNumberOfBuilds));
        builder.append(String.format(GREEN_PART, greenBuilds.getNumberOfTimes(), greenBuilds.getTimeStatistic()));
        builder.append(String.format(RED_PART, redBuilds.getNumberOfTimes(), redBuilds.getTimeStatistic()));

        Map.Entry<String, Integer> redPusher = getMostProductive(redPusherStatistic);
        if (redPusher != null) {
            builder.append(String.format(RED_PUSHER, redPusher.getKey(), redPusher.getValue()));
        }

        Map.Entry<String, Integer> greenPusher = getMostProductive(greenPusherStatistic);
        if (greenPusher != null) {
            builder.append(String.format(GREEN_PUSHER, greenPusher.getKey(), greenPusher.getValue()));
        }

        builder.append(BYE);
        return builder.toString();
    }

    public String formatHTMLReport() {
        // For demo
        StringBuilder builder = new StringBuilder();
        builder.append(String.format(HTML_HEAD, totalNumberOfBuilds));
        builder.append(String.format(HTML_GREEN_PART, greenBuilds.getNumberOfTimes(), greenBuilds.getTimeStatistic()));
        builder.append(String.format(HTML_RED_PART, redBuilds.getNumberOfTimes(), redBuilds.getTimeStatistic()));

        Map.Entry<String, Integer> redPusher = getMostProductive(redPusherStatistic);
        if (redPusher != null) {
            builder.append(String.format(HTML_RED_PUSHER, redPusher.getKey(), redPusher.getValue()));
        }

        Map.Entry<String, Integer> greenPusher = getMostProductive(greenPusherStatistic);
        if (greenPusher != null) {
            builder.append(String.format(HTML_GREEN_PUSHER, greenPusher.getKey(), greenPusher.getValue()));
        }

        builder.append(HTML_BYE);
        return builder.toString();
    }

    public String getSubject() {
        return "Daily report";
    }

    public boolean isNotEmpty() {
        return totalNumberOfBuilds != 0;
    }
}

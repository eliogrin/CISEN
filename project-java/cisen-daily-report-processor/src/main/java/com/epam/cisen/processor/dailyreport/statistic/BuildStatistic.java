package com.epam.cisen.processor.dailyreport.statistic;


import com.epam.cisen.core.api.dto.CiReport;

public class BuildStatistic {

    private static final int SECOND = 1000;
    private static final int MINUTE = 60 * SECOND;
    private static final int HOUR = 60 * MINUTE;
    private static final int DAY = 24 * HOUR;

    private int numberOfTimes;
    private int totalDuration;

    public void addReport(CiReport report) {
        numberOfTimes += 1;
        totalDuration += report.getDuration();
    }

    public int getNumberOfTimes() {
        return numberOfTimes;
    }

    public String getTimeStatistic() {
        StringBuilder text = new StringBuilder();
        if (totalDuration > DAY) {
            text.append(totalDuration / DAY).append(" days and ");
            totalDuration %= DAY;
        }
        if (totalDuration > HOUR) {
            text.append(totalDuration / HOUR).append(" hours and ");
            totalDuration %= HOUR;
        }
        if (totalDuration > MINUTE) {
            text.append(totalDuration / MINUTE).append(" minutes and ");
            totalDuration %= MINUTE;
        }
        if (totalDuration > SECOND) {
            text.append(totalDuration / SECOND).append(" seconds;");
            totalDuration %= SECOND;
        }
        return text.toString();
    }
}

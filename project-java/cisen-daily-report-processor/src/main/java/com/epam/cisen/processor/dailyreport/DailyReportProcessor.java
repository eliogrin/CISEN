package com.epam.cisen.processor.dailyreport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.cisen.core.api.AbstractProcessor;
import com.epam.cisen.core.api.Processor;
import com.epam.cisen.core.api.dto.CiReport;
import com.epam.cisen.core.api.dto.Constants;
import com.epam.cisen.processor.dailyreport.statistic.DailyStatistic;

//import java.time.Duration;
//import java.time.LocalDate;

@Component
@Service(Processor.class)
public class DailyReportProcessor extends AbstractProcessor<DailyReportProcessorConfigDTO> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DailyReportProcessor.class);

    @Override
    protected DailyReportProcessorConfigDTO getPluginTemplateConfig() {
        return new DailyReportProcessorConfigDTO();
    }

    // private volatile LocalDate lastRun;

    private volatile boolean disableForDemo = true;

    @Override
    public void process() {

        if (!disableForDemo) {
            // if (lastRun == null) {
            // lastRun = LocalDate.now();
            // } else {
            // if (Duration.between(lastRun, LocalDate.now()).toDays() == 0) {
            // return;
            // }
            // }
        }

        Map<String, DailyReportProcessorConfigDTO> jobs = getJobs();

        MongoCollection collection = mongoDBService.getCollection(Constants.DB.BUILDS);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        long timeInMills = calendar.getTimeInMillis();

        for (Map.Entry<String, DailyReportProcessorConfigDTO> entry : jobs.entrySet()) {
            String query = String.format("{ $and : [" + "   {jobId : '%s'}, " + "   {startTime : {$gt: %d}}, "
                    + "   {processed:false}" + "]}", entry.getKey(), timeInMills);
            DailyStatistic statistic = new DailyStatistic();
            try {
                MongoCursor<CiReport> dailyReports = collection.find(query).as(CiReport.class);
                if (dailyReports.count() > 0) {
                    while (dailyReports.hasNext()) {
                        CiReport report = dailyReports.next();
                        statistic.putReportToStatistic(report);
                    }
                    if (statistic.isNotEmpty()) {
                        generateReport(entry.getKey(), statistic);
                    }
                    dailyReports.close();
                    collection.update(query).with("{processed:true}");
                }
            } catch (IOException ex) {
                LOGGER.error("Fail to close db cursor", ex);
            }
        }
    }

    private void generateReport(String jobId, DailyStatistic statistic) {
        putMessage(jobId, statistic.getSubject(), statistic.formatReport());
    }

    @Override
    protected List<DailyReportProcessorConfigDTO> getTestData() {
        List<DailyReportProcessorConfigDTO> result = new ArrayList<>();
        DailyReportProcessorConfigDTO dto = new DailyReportProcessorConfigDTO();
        result.add(dto);
        return result;
    }

    @Override
    protected void setupPlugin(ComponentContext componentContext) {

    }

}
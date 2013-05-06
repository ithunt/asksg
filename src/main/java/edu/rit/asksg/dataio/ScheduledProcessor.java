package edu.rit.asksg.dataio;

import edu.rit.asksg.analytics.WordCounter;
import edu.rit.asksg.analytics.domain.WordCount;
import edu.rit.asksg.common.Log;
import edu.rit.asksg.domain.Service;
import edu.rit.asksg.service.AnalyticsService;
import edu.rit.asksg.service.AnalyticsServiceImpl;
import edu.rit.asksg.service.ProviderService;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class ScheduledProcessor {

    @Log
    Logger log;

    @Autowired
    ProviderService providerService;

    @Autowired
    AnalyticsService analyticsService;

    @Resource(name = "refreshWorker")
    AsyncWorker refreshWorker;

    @Resource(name = "subscriptionWorker")
    AsyncWorker subscriptionWorker;

    @Resource(name = "wordCounter")
    WordCounter wordCounter;


    /**
     * Launch async workers to update services.
     */
    @Scheduled(fixedDelay = 900000, initialDelay = 60000)
    public void executeRefresh() {
        log.debug("Start execution of dataio refresh");
        List<edu.rit.asksg.domain.Service> services = providerService.findAllServices();
        for (edu.rit.asksg.domain.Service service : services) {
            if (service.isEnabled()) {
                refreshWorker.work(service);
            }
        }
        log.debug("Scheduler finished dataio refresh");
    }


    @Scheduled(fixedDelay = 1100000, initialDelay = 90000)
    public void executeSubscriptions() {
        log.debug("Start execution of subscription pull");
        List<edu.rit.asksg.domain.Service> services = providerService.findAllServices();
        for (edu.rit.asksg.domain.Service service : services) {
            if (service.isEnabled()) {
                subscriptionWorker.work(service);
            }
        }
        log.debug("Scheduler finished subscription pull");
    }

    //Runs everyday, starts 3mins after startup
    @Scheduled(fixedDelay = 86400000, initialDelay = 180000)
    public void executeWordCount() {
        log.debug("Start execution of word counting");

        WordCount last = analyticsService.findLastWordCount();
        LocalDateTime start;
        if (last == null) {
            start = LocalDateTime.now().minusWeeks(1);
        } else {
            start = last.getCreated();
        }

        List<DateTime> days = AnalyticsServiceImpl.getDaySpan(start, LocalDateTime.now());
        for (DateTime d : days) {
            wordCounter.work(new LocalDateTime(d));
        }

        log.debug("Finished execution of word counting");
    }



}

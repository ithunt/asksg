package edu.rit.asksg.dataio;

import edu.rit.asksg.common.Log;
import edu.rit.asksg.service.ProviderService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class ScheduledPocessor {

	@Log
	Logger log;

	@Autowired
	ProviderService providerService;

	@Resource(name = "refreshWorker")
    AsyncWorker refreshWorker;

    @Resource(name = "subscriptionWorker")
    AsyncWorker subscriptionWorker;


	/**
	 * Launch async workers to update services.
	 */
	@Scheduled(fixedDelay = 900000, initialDelay = 1000)
	public void executeRefresh() {
		log.debug("Start execution of dataio refresh");
		List<edu.rit.asksg.domain.Service> services = providerService.findAllServices();
		for (edu.rit.asksg.domain.Service service : services) {
			refreshWorker.work(service);
		}
		log.debug("Scheduler finished dataio refresh");
	}


    @Scheduled(fixedDelay = 1100000, initialDelay = 10000)
    public void executeSubscriptions() {
        log.debug("Start execution of subscription pull");
        List<edu.rit.asksg.domain.Service> services = providerService.findAllServices();
        for (edu.rit.asksg.domain.Service service : services) {
            subscriptionWorker.work(service);
        }
        log.debug("Scheduler finished subscription pull");
    }

}

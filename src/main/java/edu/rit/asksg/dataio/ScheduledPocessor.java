package edu.rit.asksg.dataio;

import edu.rit.asksg.common.Log;
import edu.rit.asksg.service.ProviderService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduledPocessor {

	@Log
	Logger log;

	@Autowired
	ProviderService providerService;

	@Autowired
	AsyncPullWorker pullWorker;

	/**
	 * Launch async workers to update services.
	 */
	@Scheduled(fixedDelay = 900000, initialDelay = 1000)
	public void executeRefresh() {
		log.debug("Start execution of dataio refresh");
		List<edu.rit.asksg.domain.Service> services = providerService.findAllServices();
		for (edu.rit.asksg.domain.Service service : services) {
			pullWorker.work(service);
		}
		log.debug("Scheduler finished dataio refresh");
	}
}

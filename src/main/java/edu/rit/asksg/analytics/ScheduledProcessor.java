package edu.rit.asksg.analytics;

import edu.rit.asksg.common.Log;
import edu.rit.asksg.service.MessageService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduledProcessor {

	@Log
	Logger log;

	@Autowired
	MessageService messageService;

	@Autowired
	MessageWorker messageWorker;

	/**
	 * Launch workers to process messages.
	 */
	@Scheduled(fixedDelay = 900000, initialDelay = 1000)
	public void executeRefresh() {
		log.debug("Start execution of analytics messages");
		//todo: filter messages that don't have sentiment score
		List<edu.rit.asksg.domain.Message> messages = messageService.findAllMessages();
		for (edu.rit.asksg.domain.Message message : messages) {
			messageWorker.work(message);
		}
		log.debug("Scheduler finished analytics messages");
	}
}

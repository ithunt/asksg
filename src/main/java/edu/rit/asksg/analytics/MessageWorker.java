package edu.rit.asksg.analytics;

import edu.rit.asksg.common.Log;
import edu.rit.asksg.domain.Message;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageWorker {
	@Log
	Logger log;

	@Autowired
	Chatterbox chatterbox;

	public void work(final Message message) {
		chatterbox.handleMessage(message);
	}
}

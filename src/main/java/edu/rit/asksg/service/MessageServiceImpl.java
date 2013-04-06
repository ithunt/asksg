package edu.rit.asksg.service;


import edu.rit.asksg.domain.Conversation;
import edu.rit.asksg.domain.Message;
import edu.rit.asksg.domain.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;

public class MessageServiceImpl implements MessageService {

	private static final Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);

	@Autowired
	ConversationService conversationService;

	public void saveMessage(Message message) {
		//Bind the appropriate conversation to an incoming message
		if (message.getConversation() != null && message.getConversation().getId() != null) {
			try {
				message.setPosted(postMessage(message));
				conversationService.updateConversation(c);
			} catch (Exception e) {
				logger.error(e.getLocalizedMessage(),e);
				//TODO: indicate to user that the service they're using is unavailable and message might not have been sent
			}


		} else {
			messageRepository.save(message);
		}


	}


	protected boolean postMessage(Message message) {
		final Service service = message.getConversation().getService();

		return service.postContent(message);
	}


}

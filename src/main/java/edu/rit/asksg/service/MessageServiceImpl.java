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

			Conversation c = conversationService.findConversation(message.getConversation().getId());
			List<Message> messages = c.getMessages();
			message.setConversation(c);

			c.setMessages(messages);


			//TODO: make recipient hack less ugly and more formalized - pass in recipent from UI?
			try {
                //todo: sort by date
				final Message first = c.getMessagesSorted().get(0);

				if (first.getAuthor() != null)
					message.setRecipient(first.getAuthor());

				message.setPosted(postMessage(message));

				messages.add(message);
				conversationService.updateConversation(c);
			} catch (Exception e) {
				logger.error(e.getLocalizedMessage());
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

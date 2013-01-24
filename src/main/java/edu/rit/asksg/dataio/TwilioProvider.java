package edu.rit.asksg.dataio;

import edu.rit.asksg.domain.Conversation;
import edu.rit.asksg.domain.Message;
import edu.rit.asksg.domain.ProviderConfig;
import edu.rit.asksg.service.ConversationService;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TwilioProvider implements ContentProvider {

	@Autowired
	ProviderConfig config;

	@Autowired
	ConversationService conversationService;

	@Override
	public List<Conversation> fetchNewContent() {
		return new ArrayList<Conversation>();
	}

	@Override
	public List<Conversation> fetchContentSince(LocalDateTime datetime) {
		return new ArrayList<Conversation>();
	}

	@Override
	public boolean postContent(Message message) {
		return false;
	}

	@Override
	public boolean authenticate() {
		return false;
	}

	@Override
	public boolean isAuthenticated() {
		return false;
	}

	public void handleMessage(Map<String, String> request) {
		//Spring automagically gives the POST'd parameters in a Map.
		Message msg = new Message();
		msg.setCreated(LocalDateTime.now());
		msg.setAuthor(request.get("from"));
		msg.setContent(request.get("body"));

		Conversation conv = new Conversation();
		conv.getMessages().add(msg);
		conversationService.saveConversation(conv);
	}

	public void setConfig(ProviderConfig config) {
		this.config = config;
	}
}


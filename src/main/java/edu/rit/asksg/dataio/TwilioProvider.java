package edu.rit.asksg.dataio;

import edu.rit.asksg.domain.Conversation;
import edu.rit.asksg.domain.Message;
import edu.rit.asksg.domain.ProviderConfig;
import edu.rit.asksg.domain.TwilioSmsRequest;
import edu.rit.asksg.service.ConversationService;
import org.hsqldb.lib.StringInputStream;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

	public void handleMessage(TwilioSmsRequest request) {
		Message msg = new Message();
		msg.setCreated(LocalDateTime.now());
		msg.setAuthor(request.from);
		msg.setContent(request.body);

		Conversation conv = new Conversation();
		conv.getMessages().add(msg);
		conversationService.saveConversation(conv);
	}

	public void setConfig(ProviderConfig config) {
		this.config = config;
	}
}


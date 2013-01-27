package edu.rit.asksg.dataio;

import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.SmsFactory;
import com.twilio.sdk.resource.instance.Sms;
import edu.rit.asksg.domain.Conversation;
import edu.rit.asksg.domain.Message;
import edu.rit.asksg.domain.ProviderConfig;
import edu.rit.asksg.service.ConversationService;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
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
		// this assumes that the config will have our Twilio SID assigned to the username.
		TwilioRestClient twc = new TwilioRestClient(config.getUsername(), config.getAuthenticationToken());
		Map<String, String> vars = new HashMap<String, String>();
		vars.put("Body", message.getContent());
		//TODO: not sure if we'll always want this phone number
		vars.put("From", twc.getAccount().getAvailablePhoneNumbers().iterator().next().getPhoneNumber());
		vars.put("To", message.getAuthor());

		SmsFactory smsFactory = twc.getAccount().getSmsFactory();
		try {
			Sms sms = smsFactory.create(vars);
			
		} catch (TwilioRestException e) {
			return false;
		}
		return true;
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

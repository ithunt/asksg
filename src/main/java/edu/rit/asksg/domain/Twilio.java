package edu.rit.asksg.domain;

import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.SmsFactory;
import com.twilio.sdk.resource.instance.Sms;
import edu.rit.asksg.service.ConversationService;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import java.util.*;

@RooJavaBean
@RooToString
@RooJpaEntity
@RooJson
public class Twilio extends Service {

	@Autowired
	transient ProviderConfig config;

	@Autowired
	transient ConversationService conversationService;

	@Override
	public List<Conversation> getNewContent() {
		return new ArrayList<Conversation>();
	}

	@Override
	public List<Conversation> getContentSince(LocalDateTime datetime) {
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

	public void handleMessage(String smsSid, String accountSid, String from, String to, String body) {
		//Spring automagically gives the POST'd parameters in a Map.
		Message msg = new Message();
		msg.setContent(body);
		msg.setAuthor(from);

		Conversation conv = new Conversation();
		conv.setMessages(new HashSet<Message>());
		conv.getMessages().add(msg);
		msg.setConversation(conv);
		conv.setProvider(this);
		conversationService.saveConversation(conv);
	}

	public void setConfig(ProviderConfig config) {
		this.config = config;
	}


}

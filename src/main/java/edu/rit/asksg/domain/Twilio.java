package edu.rit.asksg.domain;

import edu.rit.asksg.dataio.ContentProvider;
import edu.rit.asksg.service.ConversationService;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import java.util.ArrayList;
import java.util.List;

@RooJavaBean
@RooToString
@RooJpaEntity
@RooJson
public class Twilio extends Service implements ContentProvider {

	@Autowired
	transient ConversationService conversationService;

	private transient static final Logger logger = LoggerFactory.getLogger(Twilio.class);

	@Override
	public List<Conversation> getNewContent() {
		logger.debug("Twilio does not support fetching new content.");
		return new ArrayList<Conversation>();
	}

	@Override
	public List<Conversation> getContentSince(LocalDateTime datetime) {
		return new ArrayList<Conversation>();
	}

	@Override
	public boolean postContent(Message message) {
		// this assumes that the config will have our Twilio SID assigned to the username.
		//TODO: the config won't work so this is commented out for now.
		/*TwilioRestClient twc = new TwilioRestClient(config.getUsername(), config.getAuthenticationToken());
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
		return true;*/
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

	public void handleMessage(String smsSid, String accountSid, String from, String to, String body) {

		logger.debug("Handling message: from: " + from + ", body: " + body);

		Message msg = new Message();
		msg.setContent(body);
		msg.setAuthor(from);


		Conversation conv = new Conversation(msg);
		msg.setConversation(conv);

		conv.setService(this);

		conversationService.saveConversation(conv);
	}
}

package edu.rit.asksg.web;

import edu.rit.asksg.common.Log;
import edu.rit.asksg.domain.Conversation;
import edu.rit.asksg.domain.Message;
import edu.rit.asksg.domain.ProviderConfig;
import edu.rit.asksg.domain.Service;
import edu.rit.asksg.domain.Twilio;
import edu.rit.asksg.domain.Twitter;
import edu.rit.asksg.service.ProviderService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RooWebJson(jsonObject = Conversation.class)
@Controller
@RequestMapping("/conversations")
public class ConversationController {

	@Log
	private Logger logger;

	@Autowired ProviderService providerService;

	@RequestMapping(value = "/seed")
	public ResponseEntity<String> seed() {
		Service twiloprovider = providerService.findServiceByTypeAndIdentifierEquals(Twilio.class,"37321");
		if(twiloprovider == null){
			Service twilio = new Twilio();
			ProviderConfig twilioconfig = new ProviderConfig();
			twilioconfig.setIdentifier("37321");
			twilio.setConfig(twilioconfig);
			providerService.saveService(twilio);
		}

		Conversation c = new Conversation();
		Message m1 = new Message();
		m1.setAuthor("Socrates");
		m1.setContent("For the unexamined life is not worth living");

		Set<Message> messages = new HashSet<Message>();
		messages.add(m1);

		c.setMessages(messages);
		twiloprovider = providerService.findServiceByTypeAndIdentifierEquals(Twilio.class,"37321");
		c.setProvider(twiloprovider);
		conversationService.saveConversation(c);

		HttpHeaders headers = new HttpHeaders();
		headers.add("content_type", "text/plain");

		return new ResponseEntity<String>("your seed has been spread", headers, HttpStatus.OK);
	}

	@RequestMapping(value = "/tweet")
	public ResponseEntity<String> twats() {

        Service twitter = new Twitter();
        ProviderConfig twitterConfig = new ProviderConfig();
        twitterConfig.setIdentifier("wY0Aft0Gz410RtOqOHd7Q");
        twitter.setConfig(twitterConfig);
        providerService.saveService(twitter);


        twitter = providerService.findServiceByTypeAndIdentifierEquals(Twitter.class, "wY0Aft0Gz410RtOqOHd7Q");
		List<Conversation> twats = twitter.getNewContent();
		for (Conversation c : twats) {
			conversationService.saveConversation(c);
		}

		HttpHeaders headers = new HttpHeaders();
		headers.add("content_type", "text/plain");

		return new ResponseEntity<String>("Show me your tweets!", headers, HttpStatus.OK);
	}

}


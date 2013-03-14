package edu.rit.asksg.web;

import edu.rit.asksg.common.Log;
import edu.rit.asksg.dataio.ScheduledPocessor;
import edu.rit.asksg.domain.Conversation;
import edu.rit.asksg.domain.Email;
import edu.rit.asksg.domain.Message;
import edu.rit.asksg.domain.Service;
import edu.rit.asksg.domain.SocialSubscription;
import edu.rit.asksg.domain.Twilio;
import edu.rit.asksg.domain.Twitter;
import edu.rit.asksg.domain.config.EmailConfig;
import edu.rit.asksg.domain.config.SpringSocialConfig;
import edu.rit.asksg.domain.config.TwilioConfig;
import edu.rit.asksg.service.ProviderService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RooWebJson(jsonObject = Conversation.class)
@Controller
@RequestMapping("/conversations")
public class ConversationController {

    @Log
    private Logger logger;

    @Autowired
    ProviderService providerService;

    @Autowired
    ScheduledPocessor scheduledPocessor;

    @RequestMapping(value = "/seed")
    public ResponseEntity<String> seed() {

        bootstrapProviders();

        Conversation c = new Conversation();
        Message m1 = new Message();
        m1.setAuthor("Socrates");
        m1.setContent("For the unexamined life is not worth living");

        Message m2 = new Message();
        m2.setAuthor("Tyrion Lannister");
        m2.setContent("Sorcery is the sauce fools spoon over failure to hide the flavor of the their own incompetence");

        Set<Message> messages = new HashSet<Message>();
        messages.add(m1);
        messages.add(m2);

        c.setMessages(messages);
        Service twiloprovider = providerService.findServiceByTypeAndIdentifierEquals(Twilio.class, "37321");
        c.setService(twiloprovider);
        conversationService.saveConversation(c);

        pullContent();

        HttpHeaders headers = new HttpHeaders();
        headers.add("content_type", "text/plain");

        return new ResponseEntity<String>("your seed has been spread", headers, HttpStatus.OK);
    }

    private void bootstrapProviders() {

        final Service twiloprovider = providerService.findServiceByTypeAndIdentifierEquals(Twilio.class, "37321");
        if (twiloprovider == null) {
            Service twilio = new Twilio();
            TwilioConfig twilioconfig = new TwilioConfig();
            twilioconfig.setIdentifier("37321");
            twilio.setConfig(twilioconfig);
            providerService.saveService(twilio);
        }

        final Service emailProvider = providerService.findServiceByTypeAndIdentifierEquals(Email.class, "ritasksg");
        if( emailProvider == null) {
            Service email = new Email();
            EmailConfig emailConfig = new EmailConfig();
            emailConfig.setIdentifier("ritasksg");
            emailConfig.setUsername("ritasksg@gmail.com");
            emailConfig.setPassword("allHailSpring");
            email.setConfig(emailConfig);
            providerService.saveService(email);
        }

        final Service twitterProvider = providerService.findServiceByTypeAndIdentifierEquals(Twitter.class, "wY0Aft0Gz410RtOqOHd7Q");
        if(twitterProvider == null){
            Service twitter = new Twitter();
            SpringSocialConfig twitterConfig = new SpringSocialConfig();
            twitterConfig.setIdentifier("wY0Aft0Gz410RtOqOHd7Q");

            twitterConfig.setConsumerkey("wY0Aft0Gz410RtOqOHd7Q");
            twitterConfig.setConsumersecret("rMxrTP9nqPzwU6UHIQufKR23be4w4NHIqY7VbwfzU");
            twitterConfig.setAccesstoken("15724679-FUz0huThLIpEzm66QySG7exllaV1CWV9VqXxXeTOw");
            twitterConfig.setAccesstokensecret("rFTEFz8tNX71V2nCo6pDtF38LhDEfO2f692xxzQxaA");

            twitter.setConfig(twitterConfig);

            SocialSubscription ritsg = new SocialSubscription();
            ritsg.setHandle("RIT_SG");

            Set<SocialSubscription> subscriptions = new HashSet<SocialSubscription>();
            subscriptions.add(ritsg);

            twitterConfig.setSubscriptions(subscriptions);

            providerService.saveService(twitter);
        }
    }

    @RequestMapping(value = "/tweet")
    public ResponseEntity<String> twats() {

        Service twitter = providerService.findServiceByTypeAndIdentifierEquals(Twitter.class, "wY0Aft0Gz410RtOqOHd7Q");
        List<Conversation> twats = twitter.getNewContent();
        for (Conversation c : twats) {
            //conversationService.saveConversation(c);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("content_type", "text/plain");

        return new ResponseEntity<String>("Show me your tweets!", headers, HttpStatus.OK);
    }

    protected void pullContent() {
        scheduledPocessor.executeRefresh();
        scheduledPocessor.executeSubscriptions();
    }

}

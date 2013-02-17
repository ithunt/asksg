package edu.rit.asksg.web;

import edu.rit.asksg.domain.Conversation;
import edu.rit.asksg.domain.Message;
import edu.rit.asksg.domain.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @Resource
    Map<String, Service> providerMap;

    private static final Logger logger = LoggerFactory.getLogger(ConversationController.class);

    private transient boolean strapped = false;

    @RequestMapping(value = "/seed")
    public ResponseEntity<String> seed() {

        if(!strapped) conversationService.bootstrap();

        Conversation c = new Conversation();
        Message m1 = new Message();
        m1.setAuthor("Socrates");
        m1.setContent("For the unexamined life is not worth living");

        Set<Message> messages = new HashSet<Message>();
        messages.add(m1);

        c.setMessages(messages);
        c.setProvider(providerMap.get("default"));

        conversationService.saveConversation(c);

        HttpHeaders headers = new HttpHeaders();
        headers.add("content_type", "text/plain");

        return new ResponseEntity<String>("your seed has been spread", headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/tweet")
    public ResponseEntity<String> twats() {

        Service twitter = providerMap.get("twitter");
        List<Conversation> twats = twitter.getNewContent();
        for(Conversation c : twats) {
            conversationService.saveConversation(c);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("content_type", "text/plain");

        return new ResponseEntity<String>("Show me your tweets!", headers, HttpStatus.OK);
    }

}


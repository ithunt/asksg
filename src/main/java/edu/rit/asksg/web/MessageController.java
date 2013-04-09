package edu.rit.asksg.web;

import edu.rit.asksg.common.Log;
import edu.rit.asksg.dataio.ContentProvider;
import edu.rit.asksg.domain.Conversation;
import edu.rit.asksg.domain.Email;
import edu.rit.asksg.domain.Message;
import edu.rit.asksg.service.ProviderService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RooWebJson(jsonObject = Message.class)
@Controller
@RequestMapping("/messages")
public class MessageController {

    @Resource(name = "emailProvider")
    ContentProvider emailProvider;

    @Autowired
    ProviderService providerService;

    @Log
    Logger logger;

    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJson(@RequestBody String json, Principal principal) {
        logger.error("Principal user: " + principal.getName());
     /*
            Conversation c = conversationService.findConversation(message.getConversation().getId());
			message.setConversation(c);
			c.getMessages().add(message);

    	*/
        logger.error("JSON input: " + json);
        Message message = Message.fromJsonToMessage(json);
        logger.error("Message object : " + message.toString());
        message.setAuthor(principal.getName());
        messageService.saveMessage(message);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/test")
    public ResponseEntity<String> test() {

        Conversation c = new Conversation();
        c.setId(1L);
        c.setService(providerService.findServiceByTypeAndIdentifierEquals(Email.class, "ritasksg@gmail.com"));

        Message m = new Message();
        m.setContent("Education is the path from cocky ignorance to miserable uncertainty");
        m.setAuthor("Mark Twain");
        m.setConversation(c);

        messageService.saveMessage(m);

        HttpHeaders headers = new HttpHeaders();
        headers.set("content_type", "text/plain");

        return new ResponseEntity<String>("Did it work?", headers, HttpStatus.OK);

    }


    @RequestMapping(value = "/seed")
    public ResponseEntity<String> seed() {

        Conversation convo = new Conversation();
        List<Message> messages = new ArrayList<Message>();
        convo.setService(providerService.findServiceByTypeAndIdentifierEquals(Email.class, "ritasksg@gmail.com"));

        Message m = new Message();
        m.setContent("Education is the path from cocky ignorance to miserable uncertainty");
        m.setAuthor("Mark Twain");
        m.setConversation(convo);

        messages.add(m);
        convo.setMessages(messages);

        messageService.saveMessage(m);

        HttpHeaders headers = new HttpHeaders();
        headers.set("content_type", "text/plain");

        return new ResponseEntity<String>("Your seed has been spread", headers, HttpStatus.OK);
    }


}

package edu.rit.asksg.web;

import edu.rit.asksg.common.Log;
import edu.rit.asksg.dataio.ContentProvider;
import edu.rit.asksg.domain.Conversation;
import edu.rit.asksg.domain.Email;
import edu.rit.asksg.domain.Message;
import edu.rit.asksg.service.ConversationService;
import edu.rit.asksg.service.ProviderService;
import org.json.JSONException;
import org.json.JSONObject;
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

    @Autowired
    ConversationService conversationService;

    @Log
    Logger logger;

    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJson(@RequestBody String json, Principal principal) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        String content;
        Long conversationId;
        try {
            final JSONObject parsedJson = new JSONObject(json);
            content = (String) parsedJson.get("content");
            final Double id = Double.parseDouble(parsedJson.get("conversation").toString());
            conversationId = id.longValue();
        } catch (JSONException e) {
            logger.error("JSON Parsing failed on send message with Json string: " + json);
            return new ResponseEntity<String>(headers, HttpStatus.BAD_REQUEST);
        }
        Conversation conversation = conversationService.findConversation(conversationId);
        Message message = new Message();
        message.setContent(content);
        message.setAuthor(principal.getName());
        message.setConversation(conversation);

        conversation.getMessages().add(message);
        messageService.saveMessage(message);
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

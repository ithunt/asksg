package edu.rit.asksg.web;

import edu.rit.asksg.dataio.ContentProvider;
import edu.rit.asksg.domain.Conversation;
import edu.rit.asksg.domain.Message;
import edu.rit.asksg.domain.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

@RooWebJson(jsonObject = Message.class)
@Controller
@RequestMapping("/messages")
public class MessageController {

    @Resource
    Map<String, Service> providerMap;

    @Resource(name = "emailProvider")
    ContentProvider emailProvider;

    @RequestMapping(value = "/test")
    public ResponseEntity<String> test() {

        Conversation c = new Conversation();
        c.setId(1L);
        c.setProvider(providerMap.get("default"));

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
        Set<Message> messages = new HashSet<Message>();
        convo.setProvider(providerMap.get("default"));

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


    @RequestMapping(value = "/email")
    public ResponseEntity<String> email() {

        Message m = new Message();
        m.setAuthor("ithunt0@gmail.com");
        m.setContent("Greetings from AskSG");

        emailProvider.postContent(m);


        HttpHeaders headers = new HttpHeaders();
        headers.set("content_type", "text/plain");

        return new ResponseEntity<String>("Check your email", headers, HttpStatus.OK);



    }
}

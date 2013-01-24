package edu.rit.asksg.web;

import edu.rit.asksg.domain.Conversation;
import edu.rit.asksg.domain.Message;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.Set;
import java.util.HashSet;

@RooWebJson(jsonObject = Message.class)
@Controller
@RequestMapping("/messages")
public class MessageController {

    @RequestMapping(value = "/seed")
    public ResponseEntity<String> seed() {

        Conversation convo = new Conversation();
        Set<Message> messages = new HashSet<Message>();

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

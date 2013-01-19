package edu.rit.asksg.web;

import edu.rit.asksg.domain.Conversation;
import edu.rit.asksg.domain.Message;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashSet;
import java.util.Set;

@RooWebJson(jsonObject = Conversation.class)
@Controller
@RequestMapping("/conversations")
public class ConversationController {


    @RequestMapping(value = "/seed", method = RequestMethod.GET)
    public ResponseEntity<String> seed() {


        Conversation c1 = new Conversation();
        Message m1 = new Message();

        m1.setAuthor("Plato");
        m1.setContent("The unexamined life is not worth livng for man");

        Message m2 = new Message();
        m2.setAuthor("Murphy");
        m2.setContent("Anything that can go wrong will");

        Set<Message> messages = new HashSet<Message>();
        messages.add(m1);
        messages.add(m2);

        c1.setMessages(messages);


        conversationService.saveConversation(c1);


        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "text/plain");
        return new ResponseEntity<String>("Your seed has been spread", headers, HttpStatus.OK);

    }


}

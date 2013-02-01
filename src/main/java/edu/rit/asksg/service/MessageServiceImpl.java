package edu.rit.asksg.service;


import edu.rit.asksg.domain.Conversation;
import edu.rit.asksg.domain.Message;
import edu.rit.asksg.domain.Service;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

public class MessageServiceImpl implements MessageService {

    @Autowired
    ConversationService conversationService;

    public void saveMessage(Message message) {
        LocalDateTime now = new LocalDateTime();
        message.setCreated(now);
        message.setModified(now);


        //Bind the appropriate conversation to an incoming message
        if(message.getConversation() != null && message.getConversation().getId() != null) {

            Conversation c = conversationService.findConversation( message.getConversation().getId() );
            Set<Message> messages = c.getMessages();
            message.setConversation(c);
            messages.add(message);
            c.setMessages(messages);

            message.setPosted(postMessage(message));

            conversationService.updateConversation(c);
        } else {
            messageRepository.save(message);
        }


    }


    protected boolean postMessage(Message message) {

        final Service service = message.getConversation().getProvider();

        return service.postContent(message);
    }


}

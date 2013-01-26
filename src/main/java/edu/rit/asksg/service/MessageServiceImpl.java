package edu.rit.asksg.service;


import edu.rit.asksg.domain.Conversation;
import edu.rit.asksg.domain.Message;
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

        if(message.getConversation() != null && message.getConversation().getId() != null) {

            Conversation c = conversationService.findConversation( message.getConversation().getId() );
            Set<Message> messages = c.getMessages();
            message.setConversation(c);
            messages.add(message);

            c.setMessages(messages);

            conversationService.updateConversation(c);
        } else {
            messageRepository.save(message);
        }


    }
}

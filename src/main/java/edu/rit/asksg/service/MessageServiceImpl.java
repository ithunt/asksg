package edu.rit.asksg.service;


import edu.rit.asksg.domain.Conversation;
import edu.rit.asksg.domain.Message;
import edu.rit.asksg.domain.Service;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

public class MessageServiceImpl implements MessageService {

    private static final Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);

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

            c.setMessages(messages);


            //todo: make recipient hack less ugly and more formalized - pass in recipent from UI?
            try {
                final Message first = ((Message)messages.toArray()[0]);

                if(first.getAuthor() != null)
                    message.setRecipient(first.getAuthor());

                message.setPosted(postMessage(message));

                messages.add(message);
                conversationService.updateConversation(c);
            } catch (Exception e) {
                logger.error(e.getLocalizedMessage());
            }


        } else {
            messageRepository.save(message);
        }


    }


    protected boolean postMessage(Message message) {
        final Service service = message.getConversation().getProvider();

        return service.postContent(message);
    }


}

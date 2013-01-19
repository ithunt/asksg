package edu.rit.asksg.service;


import edu.rit.asksg.domain.Conversation;
import edu.rit.asksg.domain.Message;
import org.joda.time.LocalDateTime;

public class ConversationServiceImpl implements ConversationService {


    public void saveConversation(Conversation conversation) {

        final LocalDateTime now = new LocalDateTime();

        conversation.setCreated(now);
        conversation.setModified(now);
        for (Message m : conversation.getMessages()) {
            m.setCreated(now);
            m.setModified(now);
        }


        conversationRepository.save(conversation);
    }

}

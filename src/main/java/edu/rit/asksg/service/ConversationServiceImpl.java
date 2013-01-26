package edu.rit.asksg.service;


import edu.rit.asksg.domain.Conversation;
import edu.rit.asksg.domain.Message;
import edu.rit.asksg.repository.MessageRepository;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;

public class ConversationServiceImpl implements ConversationService {

    @Autowired
    MessageService messageService;

    @Autowired
    MessageRepository messageRepository;

    public void saveConversation(Conversation conversation) {
        LocalDateTime now = new LocalDateTime();
        conversation.setCreated(now);
        conversation.setModified(now);

        for(Message m : conversation.getMessages()){
            messageService.saveMessage(m);
        }

        conversationRepository.save(conversation);
    }

    public Conversation updateConversation(Conversation conversation) {
        LocalDateTime now = new LocalDateTime();
        conversation.setModified(now);

//        for(Message m : conversation.getMessages()) {
//            if(m.getId() == null) messageRepository.save(m);
//        }

        return conversationRepository.save(conversation);
    }

}

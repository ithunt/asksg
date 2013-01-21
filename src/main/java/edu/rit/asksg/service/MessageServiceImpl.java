package edu.rit.asksg.service;


import edu.rit.asksg.domain.Message;
import org.joda.time.LocalDateTime;

public class MessageServiceImpl implements MessageService {



    public void saveMessage(Message message) {

        LocalDateTime now = new LocalDateTime();
        message.setCreated(now);
        message.setModified(now);
        messageRepository.save(message);
    }
}

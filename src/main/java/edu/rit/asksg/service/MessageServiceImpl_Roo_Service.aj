// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package edu.rit.asksg.service;

import edu.rit.asksg.domain.Message;
import edu.rit.asksg.repository.MessageRepository;
import edu.rit.asksg.service.MessageServiceImpl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

privileged aspect MessageServiceImpl_Roo_Service {
    
    declare @type: MessageServiceImpl: @Service;
    
    declare @type: MessageServiceImpl: @Transactional;
    
    @Autowired
    MessageRepository MessageServiceImpl.messageRepository;
    
    public long MessageServiceImpl.countAllMessages() {
        return messageRepository.count();
    }
    
    public void MessageServiceImpl.deleteMessage(Message message) {
        messageRepository.delete(message);
    }
    
    public Message MessageServiceImpl.findMessage(Long id) {
        return messageRepository.findOne(id);
    }
    
    public List<Message> MessageServiceImpl.findAllMessages() {
        return messageRepository.findAll();
    }
    
    public List<Message> MessageServiceImpl.findMessageEntries(int firstResult, int maxResults) {
        return messageRepository.findAll(new org.springframework.data.domain.PageRequest(firstResult / maxResults, maxResults)).getContent();
    }
    
    public Message MessageServiceImpl.updateMessage(Message message) {
        return messageRepository.save(message);
    }
    
}

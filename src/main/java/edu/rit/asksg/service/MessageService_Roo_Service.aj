// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package edu.rit.asksg.service;

import edu.rit.asksg.domain.Message;
import edu.rit.asksg.service.MessageService;
import java.util.List;

privileged aspect MessageService_Roo_Service {
    
    public abstract long MessageService.countAllMessages();    
    public abstract void MessageService.deleteMessage(Message message);    
    public abstract Message MessageService.findMessage(Long id);    
    public abstract List<Message> MessageService.findAllMessages();    
    public abstract List<Message> MessageService.findMessageEntries(int firstResult, int maxResults);    
    public abstract void MessageService.saveMessage(Message message);    
    public abstract Message MessageService.updateMessage(Message message);    
}
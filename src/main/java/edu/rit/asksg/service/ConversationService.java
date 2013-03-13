package edu.rit.asksg.service;

import edu.rit.asksg.domain.Conversation;
import org.springframework.roo.addon.layers.service.RooService;

import java.util.Collection;

@RooService(domainTypes = {edu.rit.asksg.domain.Conversation.class})
public interface ConversationService {
    public void saveConversations(Collection<Conversation> conversations);
}
